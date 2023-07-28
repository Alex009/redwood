/*
 * Copyright (C) 2023 Square, Inc.
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
package app.cash.redwood.layout.widget.yoga

import app.cash.redwood.layout.api.CrossAxisAlignment
import app.cash.redwood.layout.api.MainAxisAlignment
import app.cash.redwood.layout.widget.FlexContainer
import app.cash.redwood.ui.Density
import app.cash.redwood.ui.Margin
import app.cash.redwood.yoga.Node

interface YogaFlexContainer<W : Any> : FlexContainer<W> {
  val rootNode: Node
  val density: Density

  override fun margin(margin: Margin) {
    with(rootNode) {
      with(density) {
        marginStart = margin.start.toPx().toFloat()
        marginEnd = margin.end.toPx().toFloat()
        marginTop = margin.top.toPx().toFloat()
        marginBottom = margin.bottom.toPx().toFloat()
      }
    }
  }

  override fun crossAxisAlignment(crossAxisAlignment: CrossAxisAlignment) {
    rootNode.alignItems = crossAxisAlignment.toAlignItems()
  }

  override fun mainAxisAlignment(mainAxisAlignment: MainAxisAlignment) {
    rootNode.justifyContent = mainAxisAlignment.toJustifyContent()
  }
}
