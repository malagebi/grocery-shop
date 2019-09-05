package com.grocery.groceryshop.trainticket.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TicketListResp {

  @ApiModelProperty(value = "车次")
  private String trainName;

  @ApiModelProperty(value = "开始站")
  private String fromStation;

  @ApiModelProperty(value = "到达站")
  private String toStation;

  @ApiModelProperty(value = "出发时间")
  private String strDepartureTime;

  @ApiModelProperty(value = "到达时间")
  private String strArrivalsTime;

  @ApiModelProperty(value = "历时")
  private String duration;

  @ApiModelProperty(value = "商务座特等座")
  private String swNum;

  @ApiModelProperty(value = "一等座")
  private String fwNum;

  @ApiModelProperty(value = "二等座")
  private String sewNum;

  @ApiModelProperty(value = "高级软卧")
  private String gjrwNum;

  @ApiModelProperty(value = "软卧,一等卧")
  private String rwNum;

  @ApiModelProperty(value = "动卧")
  private String dwNum;

  @ApiModelProperty(value = "硬卧，二等卧")
  private String ywNum;

  @ApiModelProperty(value = "软座")
  private String rzNum;

  @ApiModelProperty(value = "硬座")
  private String yzNum;

  @ApiModelProperty(value = "无座")
  private String wzNum;
}
