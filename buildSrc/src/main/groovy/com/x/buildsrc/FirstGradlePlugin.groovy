package com.x.buildsrc

import com.android.build.gradle.BaseExtension
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
        def baseExtension = target.extensions.getByType(BaseExtension.class)
        def firstTransform = new FirstTransform()
        baseExtension.registerTransform(firstTransform)
    }
}
