package com.x.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project;

class FirstGradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println '插件UselessPlugin 已配置'
        def extension = target.extensions.create('firstPlugin', FirstExtension)
        target.afterEvaluate {
            println extension.name
        }
    }
}
