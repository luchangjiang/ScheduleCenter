package com.giveu.entity;

import java.util.Date;

/**
 * 合同实体类
 * Created by fox on 2018/7/18.
 */
public class CsCredit {

	private Long id;
	// 合同号
	private Long contractNo;
	// 申请日期
	private Date appDate;
	// 放款日期
	private Date loanDate;
	// 合同状态(15)
	private String status;
	private Date updateTime;
	private String updateIp;
	// 产品ID,关联Product表
	private Long idProduct;
	// 贷款金额
	private Double creditAmount;
	// 首付
	private Double initPay;
	// 商品价格
	private Double price;
	// 门店ID,关联sellerplace表
	private Long idSellerplace;
	// 销售代表ID,关联dafy_oa.sys_user_list表
	private Long idSa;
	// 内部代码 1-暗示好客户    3-暗示不好的客户
	private String interCode;
	// 合同类别,来自product表   SS-消费贷;   SC-现金贷
	private String creditType;
	private Long idPerson;
	// 销售代表备注
	private String saComments;
	// 拒绝原因
	private String reason;
	// 提交时间
	private Date commitTime;
	// 月还款额
	private Double annuity;
	// 注册时间
	private Date regTime;
	// 通过时间
	private Date approveTime;
	// 是否移动门店  0-否;    1-是
	private Long shop;
	// 下次催收日期
	private Date collectionDate;
	// 决策流程ID
	private Double wfiId;
	// 决策风险组
	private Double riskGroup;
	// 延迟时间
	private Double delayMinutes;
	// 决策流程时间
	private Date wfiTime;
	// 决策分支
	private Double branchId;
	private String fLog;
	// 是否走决策引擎
	private Double decisionFlag;
	private Double updateUser;
	// 短代码
	private String shortcode;
	// 扣款时间
	private Date deductDate;
	// 合同模式
	private String creditModel;
	// 0-未Hold单    1-正在Holding
	private Double holdingFlag;
	// 0-未运行；  1-已运行
	private Double creditInsuranceFlag;
	// PDF1文件路径
	private String pdfFile1;
	// PDF2文件路径
	private String pdfFile2;
	private Date createTime;
	// 权益包选项  0-未购买  1-基础版  2-升级版  9-取消
	private Integer powerFlag;
	// 权益包费用（按月收费）
	private Double powerFee;
	// 海尔进件状态
	private Integer hairUploadFlag;
	// 转资金方的本金
	private Double lastAmout;
	// 纠错备注
	private String modifyComments;
	// 0-等额本息    1-到期还本付息
	private Integer p2pRepaymentStyle;
	// 合同审核阶段划分
	private Integer auditStage;
	private Integer ramNumber;
	private Integer version;
	// 决策随机线程数
	private Integer decisionRandom;
	// 产品还款方式 0-等额本息；1-前低后高
	private Integer repayStyle;
	// 对应新决策系统的WorkFlow表的主键
	private String workflowGuid;
	// 无纸化系统:合同签字类别
	private String signType;
	// 生成合同时的IP
	private String createIp;
	// 支付金额
	private Double payAmount;
	// 活动ID
	private Long activeId;
	// 是否分期  0=》分期 1=》不分期
	private Integer isInstalments;
	// 客户消费主表ID
	private Long consumerRecordsId;
	// 广西联通 月服务费率
	private Double gxServiceRate;
	// 广西联通 前置咨询费率
	private Double gxQzFeerate;
	// 广西联通 前置咨询费
	private Double gxQzFee;
	// 保险费用
	private Double insuranceFee;
	// 经度 原生的(gps)
	private String latitude;
	private String longitude;
	// 免还大礼包费用(手Q项目新增 包含手机延保)
	private Double accidentInsuranceFee;
	// PDF批次号
	private String batchno;
	// 0:未购买；1：已购买；2：已取消；3：已使用；4：已恢复
	private Integer accidentInsuranceFlag;
	// 数据来源渠道 1-即有钱包，2-腾讯QQ导流,3-微信导流,4-X钱包用户　,5-爱用钱包APP,6-滴滴信用付,7-滴滴滴水贷,8-零花钱额度合并,9-爱用钱包微信端,,11-洽客榕树贷,20-即有钱包零花钱补充身份证资料虚拟合同,50-56888渠道
	private Integer creditSource;
	// 客户渠道 默认 GIVEU=>即有渠道,QIAKE=>洽客渠道
	private String creditChannel;
	// 百宝箱标识 0-未购买； 1-已购买；2-取消
	private Integer treasureBoxFlag;
	// 百宝箱费用
	private Double treasureBoxFee;
	// 百宝箱套餐，关联CS_GENE_MONITORING_CONFIG.ID
	private Double treasureBoxPackage;
	// 增值服务提前定额收取
	private Double downpaymentFee;
	// 代扣策略
	private String checkoffChannel;
	// 预审内部代码
	private String paInterCode;
	// 预审销售代表备注
	private String paSaComments;
	// 商户放款优先级
	private Integer loanPriority;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContractNo() {
		return contractNo;
	}

	public void setContractNo(Long contractNo) {
		this.contractNo = contractNo;
	}

	public Date getAppDate() {
		return appDate;
	}

	public void setAppDate(Date appDate) {
		this.appDate = appDate;
	}

	public Date getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateIp() {
		return updateIp;
	}

	public void setUpdateIp(String updateIp) {
		this.updateIp = updateIp;
	}

	public Long getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Long idProduct) {
		this.idProduct = idProduct;
	}

	public Double getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Double getInitPay() {
		return initPay;
	}

	public void setInitPay(Double initPay) {
		this.initPay = initPay;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getIdSellerplace() {
		return idSellerplace;
	}

	public void setIdSellerplace(Long idSellerplace) {
		this.idSellerplace = idSellerplace;
	}

	public Long getIdSa() {
		return idSa;
	}

	public void setIdSa(Long idSa) {
		this.idSa = idSa;
	}

	public String getInterCode() {
		return interCode;
	}

	public void setInterCode(String interCode) {
		this.interCode = interCode;
	}

	public String getCreditType() {
		return creditType;
	}

	public void setCreditType(String creditType) {
		this.creditType = creditType;
	}

	public Long getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(Long idPerson) {
		this.idPerson = idPerson;
	}

	public String getSaComments() {
		return saComments;
	}

	public void setSaComments(String saComments) {
		this.saComments = saComments;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	public Double getAnnuity() {
		return annuity;
	}

	public void setAnnuity(Double annuity) {
		this.annuity = annuity;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public Long getShop() {
		return shop;
	}

	public void setShop(Long shop) {
		this.shop = shop;
	}

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	public Double getWfiId() {
		return wfiId;
	}

	public void setWfiId(Double wfiId) {
		this.wfiId = wfiId;
	}

	public Double getRiskGroup() {
		return riskGroup;
	}

	public void setRiskGroup(Double riskGroup) {
		this.riskGroup = riskGroup;
	}

	public Double getDelayMinutes() {
		return delayMinutes;
	}

	public void setDelayMinutes(Double delayMinutes) {
		this.delayMinutes = delayMinutes;
	}

	public Date getWfiTime() {
		return wfiTime;
	}

	public void setWfiTime(Date wfiTime) {
		this.wfiTime = wfiTime;
	}

	public Double getBranchId() {
		return branchId;
	}

	public void setBranchId(Double branchId) {
		this.branchId = branchId;
	}

	public String getfLog() {
		return fLog;
	}

	public void setfLog(String fLog) {
		this.fLog = fLog;
	}

	public Double getDecisionFlag() {
		return decisionFlag;
	}

	public void setDecisionFlag(Double decisionFlag) {
		this.decisionFlag = decisionFlag;
	}

	public Double getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Double updateUser) {
		this.updateUser = updateUser;
	}

	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public Date getDeductDate() {
		return deductDate;
	}

	public void setDeductDate(Date deductDate) {
		this.deductDate = deductDate;
	}

	public String getCreditModel() {
		return creditModel;
	}

	public void setCreditModel(String creditModel) {
		this.creditModel = creditModel;
	}

	public Double getHoldingFlag() {
		return holdingFlag;
	}

	public void setHoldingFlag(Double holdingFlag) {
		this.holdingFlag = holdingFlag;
	}

	public Double getCreditInsuranceFlag() {
		return creditInsuranceFlag;
	}

	public void setCreditInsuranceFlag(Double creditInsuranceFlag) {
		this.creditInsuranceFlag = creditInsuranceFlag;
	}

	public String getPdfFile1() {
		return pdfFile1;
	}

	public void setPdfFile1(String pdfFile1) {
		this.pdfFile1 = pdfFile1;
	}

	public String getPdfFile2() {
		return pdfFile2;
	}

	public void setPdfFile2(String pdfFile2) {
		this.pdfFile2 = pdfFile2;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPowerFlag() {
		return powerFlag;
	}

	public void setPowerFlag(Integer powerFlag) {
		this.powerFlag = powerFlag;
	}

	public Double getPowerFee() {
		return powerFee;
	}

	public void setPowerFee(Double powerFee) {
		this.powerFee = powerFee;
	}

	public Integer getHairUploadFlag() {
		return hairUploadFlag;
	}

	public void setHairUploadFlag(Integer hairUploadFlag) {
		this.hairUploadFlag = hairUploadFlag;
	}

	public Double getLastAmout() {
		return lastAmout;
	}

	public void setLastAmout(Double lastAmout) {
		this.lastAmout = lastAmout;
	}

	public String getModifyComments() {
		return modifyComments;
	}

	public void setModifyComments(String modifyComments) {
		this.modifyComments = modifyComments;
	}

	public Integer getP2pRepaymentStyle() {
		return p2pRepaymentStyle;
	}

	public void setP2pRepaymentStyle(Integer p2pRepaymentStyle) {
		this.p2pRepaymentStyle = p2pRepaymentStyle;
	}

	public Integer getAuditStage() {
		return auditStage;
	}

	public void setAuditStage(Integer auditStage) {
		this.auditStage = auditStage;
	}

	public Integer getRamNumber() {
		return ramNumber;
	}

	public void setRamNumber(Integer ramNumber) {
		this.ramNumber = ramNumber;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getDecisionRandom() {
		return decisionRandom;
	}

	public void setDecisionRandom(Integer decisionRandom) {
		this.decisionRandom = decisionRandom;
	}

	public Integer getRepayStyle() {
		return repayStyle;
	}

	public void setRepayStyle(Integer repayStyle) {
		this.repayStyle = repayStyle;
	}

	public String getWorkflowGuid() {
		return workflowGuid;
	}

	public void setWorkflowGuid(String workflowGuid) {
		this.workflowGuid = workflowGuid;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Long getActiveId() {
		return activeId;
	}

	public void setActiveId(Long activeId) {
		this.activeId = activeId;
	}

	public Integer getIsInstalments() {
		return isInstalments;
	}

	public void setIsInstalments(Integer isInstalments) {
		this.isInstalments = isInstalments;
	}

	public Long getConsumerRecordsId() {
		return consumerRecordsId;
	}

	public void setConsumerRecordsId(Long consumerRecordsId) {
		this.consumerRecordsId = consumerRecordsId;
	}

	public Double getGxServiceRate() {
		return gxServiceRate;
	}

	public void setGxServiceRate(Double gxServiceRate) {
		this.gxServiceRate = gxServiceRate;
	}

	public Double getGxQzFeerate() {
		return gxQzFeerate;
	}

	public void setGxQzFeerate(Double gxQzFeerate) {
		this.gxQzFeerate = gxQzFeerate;
	}

	public Double getGxQzFee() {
		return gxQzFee;
	}

	public void setGxQzFee(Double gxQzFee) {
		this.gxQzFee = gxQzFee;
	}

	public Double getInsuranceFee() {
		return insuranceFee;
	}

	public void setInsuranceFee(Double insuranceFee) {
		this.insuranceFee = insuranceFee;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Double getAccidentInsuranceFee() {
		return accidentInsuranceFee;
	}

	public void setAccidentInsuranceFee(Double accidentInsuranceFee) {
		this.accidentInsuranceFee = accidentInsuranceFee;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public Integer getAccidentInsuranceFlag() {
		return accidentInsuranceFlag;
	}

	public void setAccidentInsuranceFlag(Integer accidentInsuranceFlag) {
		this.accidentInsuranceFlag = accidentInsuranceFlag;
	}

	public Integer getCreditSource() {
		return creditSource;
	}

	public void setCreditSource(Integer creditSource) {
		this.creditSource = creditSource;
	}

	public String getCreditChannel() {
		return creditChannel;
	}

	public void setCreditChannel(String creditChannel) {
		this.creditChannel = creditChannel;
	}

	public Integer getTreasureBoxFlag() {
		return treasureBoxFlag;
	}

	public void setTreasureBoxFlag(Integer treasureBoxFlag) {
		this.treasureBoxFlag = treasureBoxFlag;
	}

	public Double getTreasureBoxFee() {
		return treasureBoxFee;
	}

	public void setTreasureBoxFee(Double treasureBoxFee) {
		this.treasureBoxFee = treasureBoxFee;
	}

	public Double getTreasureBoxPackage() {
		return treasureBoxPackage;
	}

	public void setTreasureBoxPackage(Double treasureBoxPackage) {
		this.treasureBoxPackage = treasureBoxPackage;
	}

	public Double getDownpaymentFee() {
		return downpaymentFee;
	}

	public void setDownpaymentFee(Double downpaymentFee) {
		this.downpaymentFee = downpaymentFee;
	}

	public String getCheckoffChannel() {
		return checkoffChannel;
	}

	public void setCheckoffChannel(String checkoffChannel) {
		this.checkoffChannel = checkoffChannel;
	}

	public String getPaInterCode() {
		return paInterCode;
	}

	public void setPaInterCode(String paInterCode) {
		this.paInterCode = paInterCode;
	}

	public String getPaSaComments() {
		return paSaComments;
	}

	public void setPaSaComments(String paSaComments) {
		this.paSaComments = paSaComments;
	}

	public Integer getLoanPriority() {
		return loanPriority;
	}

	public void setLoanPriority(Integer loanPriority) {
		this.loanPriority = loanPriority;
	}
}
