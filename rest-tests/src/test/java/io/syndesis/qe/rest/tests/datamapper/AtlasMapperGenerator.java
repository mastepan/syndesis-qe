package io.syndesis.qe.rest.tests.datamapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.atlasmap.java.v2.JavaClass;
import io.atlasmap.json.v2.InspectionType;
import io.atlasmap.json.v2.JsonDataSource;
import io.atlasmap.json.v2.JsonInspectionRequest;
import io.atlasmap.json.v2.JsonInspectionResponse;
import io.atlasmap.v2.AtlasMapping;
import io.atlasmap.v2.BaseMapping;
import io.atlasmap.v2.DataSource;
import io.atlasmap.v2.DataSourceType;
import io.atlasmap.v2.Field;
import io.atlasmap.v2.LookupTables;
import io.atlasmap.v2.Mapping;
import io.atlasmap.v2.MappingType;
import io.atlasmap.v2.Mappings;
import io.atlasmap.v2.Properties;
import io.atlasmap.xml.v2.XmlDataSource;
import io.syndesis.common.model.DataShape;
import io.syndesis.common.model.DataShapeKinds;
import io.syndesis.common.model.integration.Step;
import io.syndesis.common.model.integration.StepKind;
import io.syndesis.qe.endpoints.AtlasmapEndpoint;
import io.syndesis.qe.rest.tests.entities.DataMapperStepDefinition;
import io.syndesis.qe.rest.tests.entities.StepDefinition;
import io.syndesis.qe.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Mar 1, 2018 Red Hat
 *
 * @author tplevko@redhat.com
 */
@Slf4j
@Component
public class AtlasMapperGenerator {

    @Autowired
    private AtlasmapEndpoint atlasmapEndpoint;

    public AtlasMapperGenerator() {
    }

    /**
     * Using output datashape, generates jsonInspectionResponse for steps preceding atlasMapping we want to generate.
     * The jsonInspectionResponse is stored in StepDefinition.
     *
     * @param precedingSteps
     */
    private void processPrecedingSteps(List<StepDefinition> precedingSteps) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

        for (StepDefinition s : precedingSteps) {
            String stepSpecification = s.getConnectorDescriptor().get().getOutputDataShape().get().getSpecification();
            if (stepSpecification.contains("io.atlasmap.java.v2.JavaClass")) {
                // Convert JSON string to Object - temporary solution, needs more investigation
                log.debug(stepSpecification);
                try {
                    JavaClass j = mapper.readValue(stepSpecification, JavaClass.class);
                    j = mapper.readValue(stepSpecification, JavaClass.class);
                    List<Field> fields = j.getJavaFields().getJavaField().stream().map(f -> (Field) f).collect(Collectors.toList());
                    s.setInspectionResponseFields(Optional.ofNullable(fields));
                } catch (IOException e) {
                    log.error("error: {}" + e);
                }
            } else {
                JsonInspectionResponse inspectionResponse = atlasmapEndpoint.inspectJson(generateJsonInspectionRequest(stepSpecification));
                try {
                    String mapperString = mapper.writeValueAsString(inspectionResponse);
                    log.debug(mapperString);
                } catch (JsonProcessingException e) {
                    log.error("error: {}" + e);
                }
                s.setInspectionResponseFields(Optional.ofNullable(inspectionResponse.getJsonDocument().getFields().getField()));
            }
        }
    }

    /**
     * Using input datashape, generates jsonInspectionResponse for step following after atlasMapping we want to
     * generate. The jsonInspectionResponse is stored in StepDefinition.
     *
     * @param followingStep
     */
    private void processFolowingStep(StepDefinition followingStep) {
        String stepSpecification = followingStep.getConnectorDescriptor().get().getInputDataShape().get().getSpecification();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

        if (stepSpecification.contains("io.atlasmap.java.v2.JavaClass")) {
            // Convert JSON string to Object - temporary solution, needs more investigation
            log.debug(stepSpecification);
            try {
                JavaClass j = mapper.readValue(stepSpecification, JavaClass.class);
                j = mapper.readValue(stepSpecification, JavaClass.class);
                List<Field> fields = j.getJavaFields().getJavaField().stream().map(f -> (Field) f).collect(Collectors.toList());
                followingStep.setInspectionResponseFields(Optional.ofNullable(fields));
            } catch (IOException e) {
                log.error("error: {}" + e);
            }
        } else {
            JsonInspectionResponse inspectionResponse = atlasmapEndpoint.inspectJson(generateJsonInspectionRequest(stepSpecification));
            try {
                String mapperString = mapper.writeValueAsString(inspectionResponse);
                log.debug(mapperString);
            } catch (JsonProcessingException e) {
                log.error("error: {}" + e);
            }
            followingStep.setInspectionResponseFields(Optional.ofNullable(inspectionResponse.getJsonDocument().getFields().getField()));
        }
    }

    /**
     * Gets list of output data shapes for preceding steps.
     *
     * @param precedingSteps
     * @return
     */
    private List<DataSource> processSources(List<StepDefinition> precedingSteps) {
        List<DataSource> sources = new ArrayList<>();
        for (StepDefinition s : precedingSteps) {
            DataShape outDataShape = s.getConnectorDescriptor().get().getOutputDataShape().get();
            sources.add(createDataSource(outDataShape, s, DataSourceType.SOURCE));
        }
        return sources;
    }

    /**
     * Gets input data shape for specified step, which should follow after the mapping.
     *
     * @param followingStep
     * @return
     */
    private DataSource processTarget(StepDefinition followingStep) {
        DataSource target = null;
        DataShape inDataShape = followingStep.getConnectorDescriptor().get().getInputDataShape().get();
        target = createDataSource(inDataShape, followingStep, DataSourceType.TARGET);
        return target;
    }

    /**
     * Used to generate data source elements for atlasMapping. There are three types of them: Java, Json, XML.
     *
     * TODO(tplevko): update also for XML
     *
     * @param dataShape
     * @param step
     * @param dataSourceType
     * @return
     */
    private DataSource createDataSource(DataShape dataShape, StepDefinition step, DataSourceType dataSourceType) {
        DataShapeKinds dataShapeKind = dataShape.getKind();

        DataSource source = null;

        if (dataShapeKind.toString().contains("json")) {
            source = new JsonDataSource();
            source.setUri("atlas:" + "json:" + step.getStep().getId().get());
        } else if (dataShapeKind.toString().contains("java")) {
            source = new DataSource();
            source.setUri("atlas:" + "java:" + step.getStep().getId().get() + "?className=" + dataShape.getType());
        } else if (dataShapeKind.toString().contains("xml")) {
            source = new XmlDataSource();
            //TODO(tplevko): find out how should look the XML datasource definition
        }
        source.setId(step.getStep().getId().get());
        source.setDataSourceType(dataSourceType);
        return source;
    }

    public Step getAtlasMappingStep(StepDefinition mapping, List<StepDefinition> precedingSteps, StepDefinition followingStep) {

        processPrecedingSteps(precedingSteps);
        processFolowingStep(followingStep);
        List<DataMapperStepDefinition> mappings = mapping.getDataMapperDefinition().get().getDataMapperStepDefinition();

        AtlasMapping atlasMapping = new AtlasMapping();
        atlasMapping.setMappings(new Mappings());

        for (DataSource s : processSources(precedingSteps)) {
            atlasMapping.getDataSource().add(s);
        }

        atlasMapping.setName("REST." + UUID.randomUUID().toString());
        atlasMapping.setLookupTables(new LookupTables());
        atlasMapping.setProperties(new Properties());
        atlasMapping.getDataSource().add(processTarget(followingStep));
        atlasMapping.getMappings().getMapping().addAll(generateBaseMappings(mappings, precedingSteps, followingStep));

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String mapperString = null;
        try {
            mapperString = mapper.writeValueAsString(atlasMapping);
            log.debug(mapperString);
        } catch (JsonProcessingException e) {
            log.error("error: {}" + e);
        }

        final Step mapperStep = new Step.Builder()
                .stepKind(StepKind.mapper)
                .configuredProperties(TestUtils.map("atlasmapping", mapperString))
                .build();

        return mapperStep;
    }

    private List<BaseMapping> generateBaseMappings(List<DataMapperStepDefinition> mappings, List<StepDefinition> precedingSteps, StepDefinition followingStep) {
        List<BaseMapping> baseMapping = new ArrayList<>();

        for (DataMapperStepDefinition m : mappings) {
            baseMapping.add(generateMapping(m, precedingSteps, followingStep));
        }
        return baseMapping;
    }

    private Mapping generateMapping(DataMapperStepDefinition mappingDef, List<StepDefinition> precedingSteps, StepDefinition followingStep) {
        Mapping generatedMapping = null;
        if (mappingDef.getMappingType().equals(MappingType.MAP)) {
            generatedMapping = generateMapMapping(mappingDef, precedingSteps, followingStep);
        } else if (mappingDef.getMappingType().equals(MappingType.COMBINE)) {
            generatedMapping = generateCombineMapping(mappingDef, precedingSteps, followingStep);
        } else if (mappingDef.getMappingType().equals(MappingType.SEPARATE)) {
            generatedMapping = generateSeparateMapping(mappingDef, precedingSteps, followingStep);
        } else {
            throw new UnsupportedOperationException("The specified operation is not yet supported");
        }
        return generatedMapping;
    }

    private Mapping generateCombineMapping(DataMapperStepDefinition mappingDef, List<StepDefinition> precedingSteps, StepDefinition followingStep) {
        StepDefinition fromStep = precedingSteps.get(mappingDef.getFromStep() - 1);

        Mapping generatedMapping = new Mapping();
        generatedMapping.setId(UUID.randomUUID().toString());
        generatedMapping.setMappingType(MappingType.COMBINE);
        generatedMapping.setStrategy(mappingDef.getStrategy().name());

        List<Field> in = new ArrayList<>();

        for (int i = 0; i < mappingDef.getInputFields().size(); i++) {
            String def = mappingDef.getInputFields().get(i);
            Field inField = fromStep.getInspectionResponseFields().get()
                    .stream().filter(f -> f.getPath().contains(def)).findFirst().get();
            inField.setIndex(i);
            in.add(inField);
        }

        Field out = followingStep.getInspectionResponseFields().get()
                .stream().filter(f -> f.getPath().contains(mappingDef.getOutputFields().get(0))).findFirst().get();

        in.forEach(f -> f.setDocId(fromStep.getStep().getId().get()));
        out.setDocId(followingStep.getStep().getId().get());

        generatedMapping.getInputField().addAll(in);
        generatedMapping.getOutputField().add(out);
        return generatedMapping;
    }

    private Mapping generateSeparateMapping(DataMapperStepDefinition mappingDef, List<StepDefinition> precedingSteps, StepDefinition followingStep) {
        StepDefinition fromStep = precedingSteps.get(mappingDef.getFromStep() - 1);

        Mapping generatedMapping = new Mapping();
        generatedMapping.setId(UUID.randomUUID().toString());
        generatedMapping.setMappingType(MappingType.SEPARATE);
        generatedMapping.setStrategy(mappingDef.getStrategy().name());

        List<Field> out = new ArrayList<>();

        for (int i = 0; i < mappingDef.getOutputFields().size(); i++) {
            String def = mappingDef.getOutputFields().get(i);
            Field outField = followingStep.getInspectionResponseFields().get()
                    .stream().filter(f -> f.getPath().contains(def)).findFirst().get();
            outField.setIndex(i);
            out.add(outField);
        }
        Field in = fromStep.getInspectionResponseFields().get()
                .stream().filter(f -> f.getPath().contains(mappingDef.getInputFields().get(0))).findFirst().get();

        out.forEach(f -> f.setDocId(followingStep.getStep().getId().get()));
        in.setDocId(fromStep.getStep().getId().get());

        generatedMapping.getOutputField().addAll(out);
        generatedMapping.getInputField().add(in);
        return generatedMapping;
    }

    private Mapping generateMapMapping(DataMapperStepDefinition mappingDef, List<StepDefinition> precedingSteps, StepDefinition followingStep) {

        StepDefinition fromStep = precedingSteps.get(mappingDef.getFromStep() - 1);

        Mapping generatedMapping = new Mapping();
        generatedMapping.setId(UUID.randomUUID().toString());
        generatedMapping.setMappingType(MappingType.MAP);

        Field in = fromStep.getInspectionResponseFields().get()
                .stream().filter(f -> f.getPath().contains(mappingDef.getInputFields().get(0))).findFirst().get();

        Field out = followingStep.getInspectionResponseFields().get()
                .stream().filter(f -> f.getPath().contains(mappingDef.getOutputFields().get(0))).findFirst().get();

        in.setDocId(fromStep.getStep().getId().get());
        out.setDocId(followingStep.getStep().getId().get());

        generatedMapping.getInputField().add(in);
        generatedMapping.getOutputField().add(out);
        return generatedMapping;
    }

    /**
     * Creates JsonInspectionRequest object out of specified datashape specification.
     *
     * @param specification
     * @return
     */
    private JsonInspectionRequest generateJsonInspectionRequest(String specification) {

        log.debug(specification);

        JsonInspectionRequest jsonInspectReq = new JsonInspectionRequest();
        jsonInspectReq.setJsonData(specification);
        jsonInspectReq.setType(InspectionType.SCHEMA);

        return jsonInspectReq;
    }
}
