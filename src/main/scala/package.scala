package object hatedabot{

  @inline def allCatchPrintStackTrace(body: => Any){
    try{
      val r = body
    }catch{
      case e => e.printStackTrace
    }
  }

  type BLOG_URL = String
}
