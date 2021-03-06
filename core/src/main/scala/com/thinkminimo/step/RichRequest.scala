package com.thinkminimo.step

import javax.servlet.http._
import io.Source

case class RichRequest(r: HttpServletRequest) {
  import RichRequest._

  @deprecated(message = "Use HttpServletRequest.getServerName() instead")
  def host = r.getServerName

  @deprecated(message = "Use HttpServletRequest.getServerPort() instead")
  def port = Integer.toString(r.getServerPort)

  def referer = r.getHeader("Referer") match {
    case s: String => Some(s)
    case null => None
  }

  def body:String = {
    Source.fromInputStream(r.getInputStream).mkString
  }

}

