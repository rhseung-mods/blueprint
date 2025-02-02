package com.rhseung.blueprint.util

import net.minecraft.registry.tag.TagKey
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

object ModUtils {
    fun getModid(): String {
        val current: Path = Path(System.getProperty("user.dir"));

        // 프로젝트 루트 디렉토리를 찾음
        var projectRoot: Path = current;
        while (projectRoot.listDirectoryEntries().all { it.fileName.toString() != "src" }) {
            projectRoot = projectRoot.parent;
        }

        // gradle.properties 파일에서 modid를 가져옴
        val gradlePropertiesFile: File = projectRoot.resolve("gradle.properties").toFile();
        if (gradlePropertiesFile.exists()) {
            return gradlePropertiesFile.readLines()
                .find { "modid" in it }?.substringAfter("=")?.trim()
                ?: throw IllegalStateException("Could not find `modid` in `gradle.properties`");
        }
        else
            throw IllegalStateException("Could not find `gradle.properties`");
    }

    fun <T> isVanillaTag(tag: TagKey<T>): Boolean {
        return tag.id.namespace == "minecraft";
    }
}