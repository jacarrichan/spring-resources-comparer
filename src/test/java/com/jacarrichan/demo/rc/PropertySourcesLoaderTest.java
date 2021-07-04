package com.jacarrichan.demo.rc;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PropertySourcesLoaderTest {
    @Test
    public void test() throws IOException {
        PropertySourcesLoader loader = new PropertySourcesLoader();
        System.err.println(Arrays.toString(loader.getAllFileExtensions().toArray()));
        List<Resource> resourceLists = Lists.newArrayList(
                new ClassPathResource("application.properties"),
                new ClassPathResource("application.yml")
        );
        CompositePropertySource composite = new CompositePropertySource("ResourcesComparer");
        resourceLists.stream().forEach(item -> {
            try {
                composite.addPropertySource(loader.load(item));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.err.println(Arrays.toString(composite.getPropertyNames()));
        System.err.println(composite.getProperty("user.name"));
        System.err.println(composite.getProperty("spring.application.name"));
        Stream.of(loader.getPropertySources()).forEach(item -> {
            System.out.println(item);
        });
    }
}
