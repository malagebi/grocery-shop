package com.grocery.groceryshop.trainticket.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TicketListResp {

  @Schema(description = "车次")
  private String trainName;

  @Schema(description = "开始站")
  private String fromStation;

  @Schema(description = "到达站")
  private String toStation;

  @Schema(description = "出发时间")
  private String strDepartureTime;

  @Schema(description = "到达时间")
  private String strArrivalsTime;

  @Schema(description = "历时")
  private String duration;

  @Schema(description = "商务座特等座")
  private String swNum;

  @Schema(description = "一等座")
  private String fwNum;

  @Schema(description = "二等座")
  private String sewNum;

  @Schema(description = "高级软卧")
  private String gjrwNum;

  @Schema(description = "软卧,一等卧")
  private String rwNum;

  @Schema(description = "动卧")
  private String dwNum;

  @Schema(description = "硬卧，二等卧")
  private String ywNum;

  @Schema(description = "软座")
  private String rzNum;

  @Schema(description = "硬座")
  private String yzNum;

  @Schema(description = "无座")
  private String wzNum;
}
