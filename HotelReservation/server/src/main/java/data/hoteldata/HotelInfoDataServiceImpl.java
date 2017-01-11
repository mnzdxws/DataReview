package data.hoteldata;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

import data.creatID.CreateID;
import dataSuper.DataSuperClass;
import dataservice.hoteldataservice.HotelInfoDataService;
import po.HotelInfoPO;
import util.Adress;
import util.Area;
import util.ResultMsg;
/**
 * ��ȡ�Ƶ������Ϣ
 * @author T5-SK
 *
 */
public class HotelInfoDataServiceImpl extends DataSuperClass implements HotelInfoDataService{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String tableName = "hotelInfo";
	
	ArrayList<HotelInfoPO> hotelPOs;
	
	public HotelInfoDataServiceImpl() throws RemoteException{
		super();
	}
	
	/**
	 * ���ӾƵ���Ϣ
	 * @return �µľƵ�ID 
	 */
	public String insert(HotelInfoPO po) throws RemoteException{
		String newID = CreateID.getCreateID().getNewHotelID();
		ResultMsg bMsg = addToSQL(tableName, newID,po.getName(),
				po.getAddress().toString(),po.getArea().toString(),
				""+po.getLevel(),po.getIntroduction(),po.getFacility(),
				Boolean.toString(po.getIsReserved()),""+po.getScore(),""+po.getSP());
		if(bMsg == ResultMsg.SUCCESS){
			return newID;
		}else{
			return null;
		}
		
	}
	
	
	
	
	/**
	 * ���¾Ƶ���Ϣ
	 */
	public ResultMsg update(HotelInfoPO po) throws RemoteException{
		return modifyFromSQL(tableName, po.getHotelID(),po.getName(),
				po.getAddress().toString(),po.getArea().toString(),
				""+po.getLevel(),po.getIntroduction(),po.getFacility(),
				Boolean.toString(po.getIsReserved()),""+po.getScore(),""+po.getSP());
	}
	
	/**
	 * ����hotelID���ҾƵ���Ϣ
	 * @return ��Ӧ�Ƶ������Ϣ
	 */
	@Override
	public HotelInfoPO find(String ID) throws RemoteException {
		HotelInfoPO hotelInfo = findMsg(ID);
		return hotelInfo;
	}
	
		
	
	private HotelInfoPO findMsg(String ID) throws RemoteException{
		findMes = findFromSQL(tableName,ID);
		HotelInfoPO hotelInfoPO = null;
		
		if(findMes != null){
			hotelInfoPO = new HotelInfoPO(findMes.get(0), findMes.get(1), 
								Adress.valueOf(findMes.get(2)), Area.valueOf(findMes.get(3)),
								Integer.valueOf(findMes.get(4)), findMes.get(5), 
								findMes.get(6),Boolean.valueOf(findMes.get(7)),
								Double.parseDouble(findMes.get(8)),Integer.valueOf(findMes.get(9)));
		}
		
		return hotelInfoPO;
	}

	/**
	 * ��ʾ���оƵ����Ϣ
	 */
	public ArrayList<HotelInfoPO> show() throws RemoteException{
		ArrayList<HotelInfoPO> pos = new ArrayList<HotelInfoPO>();
		
		try {
			sql = "SELECT * FROM " + tableName ;
			preState = conn.prepareStatement(sql);
			result = preState.executeQuery();
			while (result.next()) {
				pos.add(new HotelInfoPO(result.getString(1), result.getString(2),
						Adress.valueOf(result.getString(3)), Area.valueOf(result.getString(4)),
						Integer.valueOf(result.getString(5)),result.getString(6),
						result.getString(7),Boolean.valueOf(result.getString(8)),
						Double.parseDouble(result.getString(9)),
						Integer.valueOf(result.getString(10))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("�����ݿ��л�ȡ���е�hotel��Ϣ����");
			return null;
		}
		
		return pos.size()==0?null:pos;
	}


	/**
	 * ͨ���Ƶ�λ�ú���Ȧ���оƵ������Ϣ�Ĳ���
	 * @return ����������Ϣ�ľƵ���Ϣ�б�
	 */
	@Override
	public ArrayList<HotelInfoPO> findByAreaAndCircle(Adress area,Area circle) throws RemoteException{
		ArrayList<HotelInfoPO> pos = new ArrayList<HotelInfoPO>();
		
		try {
			sql = "SELECT * FROM " + tableName + " WHERE address = \'" + area + "\' AND area = \'" + circle + "\'";
			preState = conn.prepareStatement(sql);
			result = preState.executeQuery();
			while (result.next()) {
				pos.add(new HotelInfoPO(result.getString(1), result.getString(2),
						Adress.valueOf(result.getString(3)), Area.valueOf(result.getString(4)),
						Integer.valueOf(result.getString(5)),result.getString(6),
						result.getString(7),Boolean.valueOf(result.getString(8)),
						Double.parseDouble(result.getString(9)),
						Integer.valueOf(result.getString(10))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("�Ƶ���Ϣ�����ݿ��в��ҳ���");
			return null;
		}
		return pos;
	}

	@Override
	public ArrayList<HotelInfoPO> findByName(String name) throws RemoteException{
		ArrayList<HotelInfoPO> pos = new ArrayList<HotelInfoPO>();
		
		try {
			sql = "SELECT * FROM " + tableName + " WHERE name = \'" + name + "\'";
			preState = conn.prepareStatement(sql);
			result = preState.executeQuery();
			while (result.next()) {
				pos.add(new HotelInfoPO(result.getString(1), result.getString(2),
						Adress.valueOf(result.getString(3)), Area.valueOf(result.getString(4)),
						Integer.valueOf(result.getString(5)),result.getString(6),
						result.getString(7),Boolean.valueOf(result.getString(8)),
						Double.parseDouble(result.getString(9)),
						Integer.valueOf(result.getString(10))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("�Ƶ���Ϣ�����ݿ��в��ҳ���");
			return null;
		}
		return pos.size()==0?null:pos;
	}

	

	

}