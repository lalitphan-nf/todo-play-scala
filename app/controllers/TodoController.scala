package controllers

import javax.inject.Inject

import anorm._
import models.Todo
import play.api.db._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc._

/**
  * Created by lalitphan on 18/7/2560.
  */
class TodoController @Inject()(db:Database) extends SimpleRestController{

  implicit val TodoWrites = Json.writes[Todo]
  implicit val TodoReads : Reads[Todo] = (
      (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "status").read[String]
    ) (Todo.apply _)

  def index = Action {
    Ok("This is Todo Application.")
  }

  def list = Action {
    db.withConnection { implicit c =>
      val parser: RowParser[Todo] = Macro.namedParser[Todo]
      val result: List[Todo] = SQL"SELECT id,name,description,status FROM Todo".as(parser.*)
      Ok(ListToJson(result))
    }
  }

  def show(id:Int) = Action {
    db.withConnection { implicit c =>
      val parser: RowParser[Todo] = Macro.namedParser[Todo]
      val result: Todo = SQL(
        """
        select id,name,description,status from Todo
        where id = {id}
        """)
        .on("id" -> id).as(parser.single)

      Ok(objectToJson(result))

    }
  }

  def create = Action (parse.json) { request =>

    val name = (request.body \ "name").as[String]
    val description = (request.body \ "description").as[String]
    val status = (request.body \ "status").as[String]

    db.withConnection { implicit c =>
      SQL("insert into todo (name,description,status) values ({name},{description},{status})")
        .on("name" -> name, "description" -> description, "status" -> status)
        .executeInsert()
    }

    Ok("create todo")
  }

  def update(id:Int) = Action (parse.json) { request =>

    val name = (request.body \ "name").as[String]
    val description = (request.body \ "description").as[String]
    val status = (request.body \ "status").as[String]

    db.withConnection { implicit c =>
      SQL("update todo set name = {name}, description = {description}, status = {status} where id = {id}")
        .on("name" -> name, "description" -> description, "status" -> status, "id" -> id)
        .executeUpdate()
    }
    Ok("update todo id: " + id)
  }

  def delete(id:Int) = Action {
    db.withConnection { implicit c =>
      SQL("delete from Todo where id = {id}").on("id" -> id).executeUpdate()

    }
    Ok("delete todo id: " + id)
  }

}
