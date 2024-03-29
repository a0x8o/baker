package com.ing.baker.runtime.scaladsl

import com.ing.baker.runtime.javadsl
import com.ing.baker.runtime.common
import com.ing.baker.runtime.common.LanguageDataStructures.ScalaApi
import com.ing.baker.types.Value

case class IngredientInstance(name: String, value: Value) extends common.IngredientInstance with ScalaApi {

  def asJava: javadsl.IngredientInstance = new javadsl.IngredientInstance(name, value)
}

