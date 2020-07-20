package com.natchuz.hub.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;

public class UUIDConverterTest {

    private static Stream<Arguments> createTestData() {
        return Stream.of(
                of(UUID.fromString("f25bb354-6f49-47a8-bfd0-e990b2562747"), "f25bb3546f4947a8bfd0e990b2562747"),
                of(UUID.fromString("8a00c754-f925-4cd1-9ef4-e4b74ac4023e"), "8a00c754f9254cd19ef4e4b74ac4023e"),
                of(UUID.fromString("46bf911c-ea51-48f8-a4e3-739cf60fb2a7"), "46bf911cea5148f8a4e3739cf60fb2a7"),
                of(UUID.fromString("a219442c-3ab2-4fa1-867c-5f41332a63e7"), "a219442c3ab24fa1867c5f41332a63e7"),
                of(UUID.fromString("ff906ae7-56f3-48e8-904d-ac955e7e60e8"), "ff906ae756f348e8904dac955e7e60e8")
        );
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    public void test(UUID uuid, String condensed) {
        String toCondensedOutput = UUIDConverter.toCondensed(uuid);
        assertEquals(toCondensedOutput, condensed);

        UUID fromCondensedOutput = UUIDConverter.fromCondensed(toCondensedOutput);
        assertEquals(uuid, fromCondensedOutput);
    }

}
