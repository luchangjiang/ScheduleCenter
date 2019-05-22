package com.giveu.dao.oracle;

import com.giveu.entity.CsCredit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by fox on 2018/7/18.
 */
public interface DafySales2DAO {

	/**
	 * 根据状态获得合同
	 * @param status
	 * @return
	 */
	List<CsCredit> getCsCreditByStatus(@Param(value="status")String status);

	/**
	 * 获得以现行的合同
	 * @return
	 */
	List<CsCredit> getContractActive();

	/**
	 * 获得已注册的合同
	 * @return
	 */
	List<CsCredit> getContractRegister();


	/**************************************合同现行部分*******************************************/
	/**
	 * 获得以现行的合同ID
	 * @return
	 */
//	@Select("select a.id from cs_credit a inner join cs_audit_log2 b on a.id=b.id_credit where a.status='a' and b.from_status='y' and b.to_status='a' and b.end_time >= trunc(sysdate)")
	@Select("select a.id from dafy_sales.cs_credit a inner join dafy_sales.cs_audit_log2 b on a.id=b.id_credit where a.status='a' and b.from_status='y' and b.to_status='a' and b.end_time >= trunc(sysdate)")
	List<Integer> getContractActiveOnlyId();

	/**
	 * 获得已注册的合同ID
	 * @return
	 */
//	@Select("select a.id from cs_credit a where a.status = 'y' and (a.id_sa is null or a.id_sa not in (select account from cs_test_account where account_type='id_sa')) and (a.id_person is null or a.id_person not in (select account from cs_test_account where account_type='id_person'))")
	@Select("select a.id from dafy_sales.cs_credit a where a.status = 'y' and (a.id_sa is null or a.id_sa not in (select account from dafy_sales.cs_test_account where account_type='id_sa')) and (a.id_person is null or a.id_person not in (select account from dafy_sales.cs_test_account where account_type='id_person'))")
	List<Integer> getContractRegisterOnlyId();


	/**************************************代扣部分*******************************************/
	/**
	 * 获得本日需要处理的代扣流水数量
	 * @return
	 */
//	@Select("select count(*) from checkoff_auto_acp a where a.create_time >= trunc(sysdate) and a.pay_status = 'a'")
	@Select("select count(*) from dafy_sales.checkoff_auto_acp a where a.create_time >= trunc(sysdate) and a.pay_status = 'a'")
	Integer getCheckOffCount();

	/**
	 * 获得本日处理成功的代扣流水数量
	 * @return
	 */
//	@Select("select count(*) from checkoff_auto_acp a where a.create_time >= trunc(sysdate) and a.pay_status='k'")
	@Select("select count(*) from dafy_sales.checkoff_auto_acp a where a.create_time >= trunc(sysdate) and a.pay_status='k'")
	Integer getCheckOffSucceededCount();



	/**************************************放款部分*******************************************/
	/**
	 * 现金贷未放款
	 * @return
	 */
	@Select("select count(1) tcount " +
			"from dafy_sales.checkoff_release  a  " +
			"join dafy_sales.cs_credit b on a.id_credit = b.id " +
			"where a.releasestatus = 'a' and b.status in('a','p','k') and a.pro_type='o'  " +
			"and  b.app_date < trunc(sysdate - #{days}) " +
			"and (b.id_sa is null or b.id_sa not in (select account from dafy_sales.cs_test_account where account_type='id_sa')) " +
			"and (b.id_person is null or b.id_person not in (select account from dafy_sales.cs_test_account where account_type='id_person')) ")
	Integer getLargeReleaseCreditCount(int days);

	/**
	 * 零花钱未放款
	 * @return
	 */
	@Select("select count(1) tcount " +
			"from dafy_sales.checkoff_release  a  " +
			"join dafy_sales.cycle_credit b on a.id_credit = b.id " +
			"where a.releasestatus = 'a' and b.status in('a','p','k') and a.pro_type='c'  " +
			"and  b.app_date < trunc(sysdate - #{days}) " +
			"and (b.id_person is null or b.id_person not in (select account from dafy_sales.cs_test_account where account_type='id_person')) ")
	Integer getPettyReleaseCreditCount(int days);


	/**
	 * 商户未放款
	 * @return
	 */
	@Select("select count(1) tcount from dafy_sales.release_credit a inner join dafy_sales.cs_credit b on a.contract_no=b.contract_no  " +
			"where a.status='a' and a.create_time<trunc(sysdate) - #{days} and b.credit_model<>'SX' and b.credit_type<>'SF' ")
	Integer getMerchReleaseCreditCount(int days);



	/**************************************提前还款部分*******************************************/
	/**
	 * --提前还款
	 * @return
	 */
	@Select("select count(*) from (select count(*) from dafy_sales.instalment a   " +
			"join dafy_sales.cs_credit b on a.id_credit=b.id  " +
			"where a.status='a' and a.paystatus='a' and a.date_due<trunc(sysdate) and a.date_due>=trunc(sysdate)-7 " +
			"and not exists(select 1 from dafy_sales.checkoff_auto_acp c where c.id_credit=a.id_credit and c.pay_status='u' and c.create_time>=trunc(sysdate)-7)  " +
			"and not exists(select 1 from dafy_sales.checkoff_command d where d.id_credit=a.id_credit and d.pay_status='u' and d.create_time>=trunc(sysdate)-7) " +
			"and a.id_credit in (select distinct id_credit from dafy_sales.instalment where status='a' and paytype=2 and paystatus='a') " +
			"group by a.id_credit having nvl(sum(value_instalment-value_pay),0)>50)")
	Integer getInstalmentCountByPayType2();

	/**
	 * --15天只还本金
	 * @return
	 */
	@Select("select count(*) from (select distinct a.id_credit,b.contract_no,b.id_person,4 paytype from dafy_sales.instalment a  " +
			"join dafy_sales.cs_credit b on a.id_credit=b.id   " +
			"where a.status='a' and a.paytype=4 and a.paystatus='a' and a.date_client<trunc(sysdate)-1 and a.date_client>trunc(sysdate)-10 " +
			"and not exists(select 1 from dafy_sales.checkoff_auto_acp c where c.id_credit=a.id_credit and c.pay_status='u' and c.pay_type=4 and c.create_time>=trunc(sysdate)-7)  " +
			"and not exists(select 1 from dafy_sales.checkoff_command d where d.id_credit=a.id_credit and d.pay_status='u' and d.create_time>=trunc(sysdate)-7))")
	Integer getInstalmentCountByPayType4();

	/**
	 * --冷静期取消
	 * @return
	 */
	@Select("select count(*) from (select distinct a.id_credit,b.contract_no,b.id_person,6,paytype from dafy_sales.instalment a  " +
			"join dafy_sales.cs_credit b on a.id_credit=b.id   " +
			"where a.status='a' and a.paytype=6 and a.paystatus='a' and a.date_client<trunc(sysdate)-1 and a.date_client>trunc(sysdate)-10 " +
			"and not exists(select 1 from dafy_sales.checkoff_auto_acp c where c.id_credit=a.id_credit and c.pay_status='u' and c.pay_type=6 and c.create_time>=trunc(sysdate)-7)  " +
			"and not exists(select 1 from dafy_sales.checkoff_command d where d.id_credit=a.id_credit and d.pay_status='u' and d.create_time>=trunc(sysdate)-7))")
	Integer getInstalmentCountByPayType6();


	/**
	 * -- 获取等待时间大于10分钟的静默开户数量
	 * @return
	 */
	@Select("select count(*)  " +
			"  from dafy_sales.cs_credit t " +
			" where t.status = 'w' " +
			"   and t.credit_model = 'P2P_TBJ' " +
			"   and t.credit_type in ('SS', 'SC') " +
			"   and (sysdate - t.update_time) * 24 * 60 > 10 ")
	Integer getOpenAccountWaitCount();


	/**
	 * -- 获取等待时间大于10分钟的静默开户 ID 列表
	 * @return
	 */
	@Select("select t.id  " +
			"  from dafy_sales.cs_credit t " +
			" where t.status = 'w' " +
			"   and t.credit_model = 'P2P_TBJ' " +
			"   and t.credit_type in ('SS', 'SC') " +
			"   and (sysdate - t.update_time) * 24 * 60 > 10 ")
	List<Long> getOpenAccountWaitList();


	/**
	 * -- 获得开户失败大于两天未处理的数量
	 * @return
	 */
	@Select("select count(*)  " +
			"  from dafy_sales.cs_credit t " +
			" where t.status = 'dw' " +
			"   and t.credit_model = 'P2P_TBJ' " +
			"   and t.credit_type in ('SS', 'SC') " +
			"   and (sysdate - t.update_time) > 3 ")
	Integer getOpenAccountFailedCount();

	/**
	 * -- 获得开户失败大于两天未处理的 ID 列表
	 * @return
	 */
	@Select("select t.id " +
			"  from dafy_sales.cs_credit t " +
			" where t.status = 'dw' " +
			"   and t.credit_model = 'P2P_TBJ' " +
			"   and t.credit_type in ('SS', 'SC') " +
			"   and (sysdate - t.update_time) > 3 ")
	List<Long> getOpenAccountFailedList();







}
