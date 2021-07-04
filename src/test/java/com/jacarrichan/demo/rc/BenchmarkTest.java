package com.jacarrichan.demo.rc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
public class BenchmarkTest {
    File workDir = new ClassPathResource("/environment").getFile();
    File[] userEnvFiles = new File(workDir.getAbsolutePath() + File.separator + "user").listFiles();

    public BenchmarkTest() throws IOException {
    }

    @Test
    public void testBenchmarkFile() {
        Map<String, Object> bmMapping = getBenchmarkMapping("dev");
        bmMapping.forEach((k, v) -> log.info("{}--{}", k, v));
    }

    @Test
    public void testComparer() throws IOException {
        for (File userEnvFile : userEnvFiles) {
            String envName = FilenameUtils.getBaseName(userEnvFile.getName());
            System.err.println("=======================================================================" + envName);
            List<Resource> resourceList = Stream.of(userEnvFile.listFiles()).map(item -> new FileSystemResource(item
            )).collect(Collectors.toList());
            Map<String, Object> userMapping = getValuesMapping(resourceList);
            Map<String, Object> benchmarkMapping = getBenchmarkMapping(envName);
            userMapping.forEach((k, v) -> {
                Object bmVal = benchmarkMapping.get(k);
                if (null != bmVal) {
                    Assert.assertEquals(k + "与基准值不一致", bmVal, v);
                }

            });
        }
    }

    private Map<String, Object> getBenchmarkMapping(String envName) {
        String bmFile = workDir.getAbsolutePath() + File.separator + "benchmark" +
                File.separator + "application-" + envName + ".yml";
        log.info("benchmark file {}", bmFile);
        Assert.assertTrue(new File(bmFile).exists());
        Map<String, Object> benchmarkMapping = getValuesMapping(Lists.newArrayList(new FileSystemResource(
                bmFile)));
        return benchmarkMapping;
    }

    private Map<String, Object> getValuesMapping(List<Resource> resourceList) {
        PropertySourcesLoader loader = new PropertySourcesLoader();
        CompositePropertySource composite = new CompositePropertySource("ResourcesComparer");
        resourceList.stream().forEach(item -> {
            try {
                composite.addPropertySource(loader.load(item));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        String[] propertyNames = composite.getPropertyNames();
        return Stream.of(propertyNames).collect(Collectors.toMap(Function.identity(), i -> composite.getProperty(i)));
    }
}
