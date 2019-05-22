package com.giveu.model;

/**
 * Created by fox on 2018/11/1.
 */
public class TopModel {

	private TopFrequencyModel topFrequencyModel;
	private TopFailModel topFailModel;
	private TopLeadTimeModel topLeadTimeModel;
	private TopObjFailModel topObjFailModel;

	public TopObjFailModel getTopObjFailModel() {
		return topObjFailModel;
	}

	public void setTopObjFailModel(TopObjFailModel topObjFailModel) {
		this.topObjFailModel = topObjFailModel;
	}

	public TopFrequencyModel getTopFrequencyModel() {
		return topFrequencyModel;
	}

	public void setTopFrequencyModel(TopFrequencyModel topFrequencyModel) {
		this.topFrequencyModel = topFrequencyModel;
	}

	public TopFailModel getTopFailModel() {
		return topFailModel;
	}

	public void setTopFailModel(TopFailModel topFailModel) {
		this.topFailModel = topFailModel;
	}

	public TopLeadTimeModel getTopLeadTimeModel() {
		return topLeadTimeModel;
	}

	public void setTopLeadTimeModel(TopLeadTimeModel topLeadTimeModel) {
		this.topLeadTimeModel = topLeadTimeModel;
	}
}
