package com.apestech.scm;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.apestech.oap.RopRequestContext;
import com.apestech.oap.event.PreDoServiceEvent;
import com.apestech.oap.event.RopEventListener;



public class SamplePreDoServiceEventListener implements RopEventListener<PreDoServiceEvent> {

	public void onRopEvent(PreDoServiceEvent ropEvent) {
		RopRequestContext ropRequestContext = ropEvent.getRopRequestContext();
		if (ropRequestContext != null) {
			Map<String, String> allParams = ropRequestContext.getAllParams();
			System.out.println(JSON.toJSON(allParams));
		}
	}

	public int getOrder() {
		return 1;
	}
}
