package com.atguigu.gmallpublish.service

object DSLs {

  def getDSL(date: String, keyword: String, startPage: Int, sizePerPage: Int, aggFild: String, aggCount: Int) =
    s"""
       |get sale_detail_1015/_search
       |{
       |  "query":{
       |    "bool": {
       |      "filter": {
       |        "term": {
       |          "dt": ${date}
       |        }
       |      },
       |      "must": [
       |        {"match": {
       |          "sku_name": {
       |            "query": ${keyword},
       |            "operator": "and"
       |          }
       |        }}
       |      ]
       |    }
       |  },
       |  "aggs":{
       |    "group_by_user_gender":{
       |      "terms": {
       |        "field": "user_gender",
       |        "size": ${aggCount}
       |      }
       |    }
       |  },
       |  "from":${(startPage - 1) * sizePerPage},
       |  "size":${sizePerPage}
       |}
       |""".stripMargin
}
