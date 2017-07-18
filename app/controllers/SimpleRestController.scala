package controllers

import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc.Controller

/**
  * Created by lalitphan on 18/7/2560.
  */
trait SimpleRestController extends Controller {
  implicit def objectToJson[T](obj: T)(implicit toJson: Writes[T]): JsValue = Json.toJson(obj)
  implicit def ListToJson[T](list: T)(implicit toJson: Writes[T]): JsValue = Json.toJson(list)
}
