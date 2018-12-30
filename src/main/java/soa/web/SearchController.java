package soa.web;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SearchController {

  private final ProducerTemplate producerTemplate;

  @Autowired
  public SearchController(ProducerTemplate producerTemplate) {

    this.producerTemplate = producerTemplate;
  }

  @RequestMapping("/")
  public String index() {

    return "index";
  }


  @RequestMapping(value = "/search")
  @ResponseBody
  public Object search(@RequestParam("q") String q) {

    int N = 0;
    if (q.contains("max:")) {
      int pos_max = q.indexOf("max");
      String values = q.substring(pos_max);
      String[] div = values.split(":");
      N = Integer.parseInt(div[1]);
    }
    // Now we create the headers
    Map<String,Object> headers = new HashMap<>();
    q = q.replaceAll("max:[0-9]*","");
    headers.put("CamelTwitterKeywords",q);
    headers.put("CamelTwitterCount",N);
    return producerTemplate.requestBodyAndHeaders("direct:search", "", headers);
  }
}