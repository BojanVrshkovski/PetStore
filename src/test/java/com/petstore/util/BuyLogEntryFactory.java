package com.petstore.util;

import com.petstore.entity.dto.BuyLogEntryDto;
import static com.petstore.util.BuyLogEntryConstants.ID;
import static com.petstore.util.BuyLogEntryConstants.USER_ID;
import static com.petstore.util.BuyLogEntryConstants.EXECUTION_DATE;
import static com.petstore.util.BuyLogEntryConstants.ALLOWED_TO_BUY;
public class BuyLogEntryFactory {
	public static BuyLogEntryDto getDefaultBuyLogEntryDto(){
		BuyLogEntryDto buyLogEntryDto = new BuyLogEntryDto(ID,EXECUTION_DATE,USER_ID,ALLOWED_TO_BUY);

		return buyLogEntryDto;
	}
}
