/*
 * Copyright (C) 2022 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.cash.redwood.tooling.codegen

import app.cash.redwood.tooling.codegen.ProtocolCodegenType.Compose
import app.cash.redwood.tooling.codegen.ProtocolCodegenType.Json
import app.cash.redwood.tooling.codegen.ProtocolCodegenType.Widget
import app.cash.redwood.tooling.schema.ProtocolSchemaSet
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

public enum class ProtocolCodegenType {
  Compose,
  Json,
  Widget,
}

public fun ProtocolSchemaSet.generate(type: ProtocolCodegenType, destination: Path) {
  when (type) {
    Compose -> {
      generateProtocolBridge(this).writeTo(destination)
      generateProtocolLayoutModifierSerialization(this).writeTo(destination)
      for (dependency in all) {
        generateProtocolWidgetFactory(dependency, host = schema).writeTo(destination)
        generateProtocolLayoutModifierSerializers(dependency, host = schema)?.writeTo(destination)
        for (widget in dependency.widgets) {
          generateProtocolWidget(dependency, widget, host = schema).writeTo(destination)
        }
      }
    }
    Json -> {
      val embeddedSchema = schema.toEmbeddedSchema()
      val path = destination.resolve(embeddedSchema.path)
      path.parent.createDirectories()
      path.writeText(embeddedSchema.json)
    }
    Widget -> {
      generateDiffConsumingNodeFactory(this).writeTo(destination)
      generateDiffConsumingLayoutModifierSerialization(this).writeTo(destination)
      for (dependency in all) {
        generateDiffConsumingLayoutModifierImpls(dependency, host = schema).writeTo(destination)
        for (widget in dependency.widgets) {
          generateDiffConsumingWidget(dependency, widget, host = schema).writeTo(destination)
        }
      }
    }
  }
}
