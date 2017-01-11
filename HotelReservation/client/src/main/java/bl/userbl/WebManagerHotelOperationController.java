package bl.userbl;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.userblservice.WebManagerHotelOperationBlService;
import dataservice.userdataservice.UserManagementDataService;
import net.RMIManage;
import util.DataServiceType;
import vo.HotelInfoVO;
import vo.StuffInfoVO;


/**
 * ��վ������Ա�ԾƵ�Ĳ���
 * @author �ܳ�
 *
 */
public class WebManagerHotelOperationController implements WebManagerHotelOperationBlService {

	private WebManager manager;
	private UserManagementDataService data;
	
	
	public WebManagerHotelOperationController(){
		data= (UserManagementDataService)RMIManage.
				getDataService(DataServiceType.UserManagementDataService);
		manager=new WebManager(data);
	}
	
	/**
	 * ���ӾƵ�	
	 * @param �Ƶ���ϢVO
	 */
	public String hotelAdd(HotelInfoVO vo){
		try {
			return manager.hotelAdd(vo);
		} catch (RemoteException e) {
			return null;
		}
	}
			
	


	/**
	 * �鿴�Ƶ��б�
	 * @return �Ƶ���Ϣ�б�
	 */
	public ArrayList<HotelInfoVO> hotelScan() {
		try {
			return manager.hotelScan();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ���ӾƵ깤����Ա
	 * @param �Ƶ�IDVO
	 * @param �û�IDVO
	 */
	public String stuffAdd(StuffInfoVO vo) {
		try {
			return manager.stuffAdd(vo);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

}