import com.gree.HttpClientUtils.get_with_params

object HttpClientTest {

  def main(args: Array[String]): Unit = {
    val url = "http://sysapp.gree.com/GreeMesOpenApi/GreeMesApi/api/services/app/MesQCData/GetQCDatas"
    val computer: Integer = 4
    val startDateTime: String = "2018-09-01"
    val endDateTime: String = "2018-09-02"
    val skipCount: Integer = 3
    val maxResultCount: Integer = 5
    println(get_with_params(url, computer, startDateTime, endDateTime, skipCount, maxResultCount))
  }
}
