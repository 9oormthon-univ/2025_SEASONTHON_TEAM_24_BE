package com.qoormthon.empty_wallet.global.common.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListResponseDTO<T> {

  private int totalCount;
  private T list;

  public static <T> ListResponseDTO<T> of(T list) {
    ListResponseDTO<T> response = new ListResponseDTO<>();
    response.setTotalCount(((java.util.Collection<?>) list).size());
    response.setList(list);
    return response;
  }

}
