package dataSuper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.ResultMsg;

/**
 * �������ݲ�ʵ�ֵĸ���
 * @author T5-SK
 *
 */

public class DataSuperClass extends UnicastRemoteObject{
	
	/**
	 * ���л�
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * �����ݿ������
	 */
	protected Connection conn;
	/**
	 * 
	 */
	protected PreparedStatement preState;
	/**
	 * ���ݿ����
	 */
	protected String sql;
	/**
	 * ���ݿ����Ӱ������
	 */
	protected ResultSet result;
	/**
	 * ���ҷ��ص���Ϣ
	 */
	protected ArrayList<String> findMes;
	/**
	 * �����ݿ������Ӱ�쵽����������Ϣ������
	 */
	protected int affectRows;
	/**
	 * 
	 */
	protected static DataServiceHelper helper = new DataServiceHelper();
	
	private static final Map<String, ArrayList<String>> SQLmap = new HashMap<String, ArrayList<String>>(50);
	
	static{
		SQLmap.put("createID", helper.bulidSQL("createID", 9,"flag","customerID","hotelStuffID","webStuffID","webManagerID","hotelID","orderID","promotionHotelID","promotionWebID"));
		SQLmap.put("credit", helper.bulidSQL("credit",6, "userID","orderID","time","action","creditChange","creditResult"));
		SQLmap.put("hotelEvaluate", helper.bulidSQL("hotelEvaluate", 6, "userID","hotelID","score","comment","reserve","orderID"));
		SQLmap.put("hotelInfo", helper.bulidSQL("hotelInfo", 10, "hotelID","name","address","area","level","introduction","facility","reserve","score","SP"));
		SQLmap.put("roomInfo", helper.bulidSQL("roomInfo", 5, "roomID","stateName","type","price","hotelID"));
		SQLmap.put("orderList", helper.bulidSQL("orderList", 14, "orderID","customerInfoPO","orderState","price","hotelID","hasChild","latestExecutionTime","checkInTime","checkOutTime","cancelledTime","roomNumber","peopleNumber","roomIDs","roomType"));
		SQLmap.put("orderRoom", helper.bulidSQL("orderRoom", 4, "hotelID","orderID","roomID","roomType"));
		SQLmap.put("promotionHotel", helper.bulidSQL("promotionHotel", 9,"promotionHotelID", "hotelID","promotionHotelType","beginTime","endTime","ratio","level","number","businessName"));
		SQLmap.put("promotionWeb", helper.bulidSQL("promotionWeb", 7, "promotionWebID","promotionWebType","beginTime","endTime","ratio","level","location"));
		SQLmap.put("contact", helper.bulidSQL("contact", 2, "phoneNumber","emailAddress"));
		SQLmap.put("customer", helper.bulidSQL("customer", 11, "userID","username", "password","contact","type","isMember","vipType","credit","orderIDList","hotelIDList","creditList"));
		SQLmap.put("loginIn", helper.bulidSQL("loginIn", 2, "username","password"));
		SQLmap.put("userInfo", helper.bulidSQL("userInfo", 5, "userID","username", "password","contact","type"));
		SQLmap.put("stuffInfo", helper.bulidSQL("stuffInfo", 6, "userID","username", "password","contact","type","hotel"));
		SQLmap.put("user-hotel", helper.bulidSQL("user-hotel", 2, "userID","hotelID"));
		SQLmap.put("levelSystem", helper.bulidSQL("levelSystem", 2, "levels","credits"));
		SQLmap.put("customerInfo", helper.bulidSQL("customerInfo", 8, "userID","username","password","contact","type","isMember","vipType","credit"));
		SQLmap.put("commonVip", helper.bulidSQL("commonVip", 7, "userID","username","password","contact","credit","birthday","vipType"));
		SQLmap.put("businessVip", helper.bulidSQL("businessVip", 7, "userID","username","password","contact","credit","businessName","vipType"));
	}
	
	public DataSuperClass() throws RemoteException {
		this.conn = DataBaseInit.getConnection();
		System.out.println("succeed to bulid dataservice");
	}
	
	/**
	 * �����ݿ�������һ������
	 * @param tableName ��������
	 * @param paras �ɱ�����б�
	 * @return
	 */
	protected ResultMsg addToSQL(String tableName , String... paras) {
		try {
			int paralen = Integer.parseInt(SQLmap.get(tableName).get(0));
			preState = conn.prepareStatement(SQLmap.get(tableName).get(1));
			for (int i = 0; i < paralen; i++) {
				preState.setString(i + 1, paras[i]);
			}
			
			affectRows = preState.executeUpdate();
		} /*catch(MySQLIntegrityConstraintViolationException e){
			return ResultMsg.hasExist;
			//����쳣�����ˣ�������
		}*/ catch (SQLException e) {
			e.printStackTrace();
			return ResultMsg.FAIL;
		}
		
		if(affectRows == 0){
			return ResultMsg.FAIL;
		}
		
		return ResultMsg.SUCCESS;
	}
	
	/**
	 * �����ݿ�������һ������,ID����Ψһ�����
	 * @param tableName ��������
	 * @param paras �ɱ�����б�
	 * @return
	 */
	protected ResultMsg addToSQLByID(String tableName , String... paras) {
		try {
			//���ID�Ѿ����ھͲ��ϴ�
			sql = "SELECT * FROM "+ tableName;
			preState = conn.prepareStatement(sql);
			result = preState.executeQuery();
			while (result.next()) {
				if(paras[0].equals(result.getString(1))){
					return ResultMsg.FAIL;
				}
			}
			int paralen = Integer.parseInt(SQLmap.get(tableName).get(0));
			preState = conn.prepareStatement(SQLmap.get(tableName).get(1));
			for (int i = 0; i < paralen; i++) {
				preState.setString(i + 1, paras[i]);
			}
			
			affectRows = preState.executeUpdate();
		} /*catch(MySQLIntegrityConstraintViolationException e){
			return ResultMsg.hasExist;
			//����쳣�����ˣ�������
		}*/ catch (SQLException e) {
			e.printStackTrace();
			return ResultMsg.FAIL;
		}
		
		if(affectRows == 0){
			return ResultMsg.FAIL;
		}
		
		return ResultMsg.SUCCESS;
	}
	
	/**
	 * �����ݿ���ɾ��һ�����ݣ������ΨһID
	 * @param tableName ��������
	 * @param ID Ҫɾ�����ݵ�ID
	 * @return
	 */
	protected ResultMsg delFromSQL(String tableName , String ID) {
		try {
			preState = conn.prepareStatement(SQLmap.get(tableName).get(2) +"\"" + ID + "\"");
			affectRows = preState.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("�޷��������ݿ�");
			return ResultMsg.FAIL;
		}
		
		if(affectRows != 1){
			System.out.println("��ǰ���ݿ���Ӱ�������Ϊ"+affectRows);
			System.out.println("�����ݿ��������Ϣ�����ڻ��ߴ��ڶ����޷���λɾ������"+tableName+"���У�IDΪ" + ID);
			return ResultMsg.NOT_EXIST;
		}
		
		return ResultMsg.SUCCESS;
	}
	
	/**
	 * �����ݿ��в���һ����Ϣ
	 * @param tableName ��������
	 * @param ID Ҫ�������ݵ�ID
	 * @return �Ҳ���ʱ����null�����򷵻�PO��������Ϣ
	 */
	protected ArrayList<String> findFromSQL(String tableName, String ID) {
		try {
			preState = conn.prepareStatement(SQLmap.get(tableName).get(3) + "\""+ ID + "\"");
			result = preState.executeQuery();
			if(result.next()) {
				// ������ҵ���Ӧ��ID
				int paralen = Integer.parseInt(SQLmap.get(tableName).get(0));
				ArrayList<String> temp = new ArrayList<String>(paralen);
				for (int i = 0; i < paralen; i++) {
					temp.add(result.getString(i + 1));
				}
				return temp;

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}
	
	/**
	 * ����û��ID��SQL����
	 * @param tableName
	 * @return
	 */
	protected ArrayList<String> findFromSQL(String tableName){
		ArrayList<String> temp = new ArrayList<String>();
		try {
			preState = conn.prepareStatement(SQLmap.get(tableName).get(3));
			result = preState.executeQuery();
			while(result.next()) {
				int paralen = Integer.parseInt(SQLmap.get(tableName).get(0));
				temp = new ArrayList<String>(paralen);
				for (int i = 0; i < paralen; i++) {
					temp.add(result.getString(i + 1));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(temp.size() == 0 ) {
			return null;
		}else{
			return temp;
		}
	}
	
	/**
	 * �޸�һ������
	 * @param tableName ��������
	 * @param newParas  PO�����б�
	 * @return 
	 */
	protected ResultMsg modifyFromSQL(String tableName , String... newParas) {
		
		try {
			int paralen = Integer.parseInt(SQLmap.get(tableName).get(0));
			preState = conn.prepareStatement(SQLmap.get(tableName).get(4) +"\"" + newParas[0]  +"\"");
			for (int i = 0; i < paralen - 1; i++) {
				preState.setString(i + 1, newParas[i + 1]);
			}
			affectRows = preState.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return ResultMsg.FAIL;
		}
		
		if(affectRows == 0){
			return ResultMsg.NOT_EXIST;
		}else if(affectRows > 1){
			return ResultMsg.FAIL;
		}
		
		return ResultMsg.SUCCESS;
	}
	
	
	/**
	 * �������������Ϣ
	 * @param tableName
	 * @return
	 */
	protected ResultMsg initialFromSQL(String tableName) {
		
		try {
			sql = SQLmap.get(tableName).get(5);
			preState = conn.prepareStatement(sql);

			preState.executeUpdate();
			return ResultMsg.SUCCESS;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ResultMsg.FAIL;
	}
	
	/**
	 * ִ����䲢����ִ�н��
	 * 
	 * @param tempPreState
	 * @return
	 */
	protected ResultMsg getDoResult(PreparedStatement tempPreState) {
		try {
			if (tempPreState.execute()) {
				return ResultMsg.SUCCESS;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ResultMsg.FAIL;
	}
	
	/**
	 * ���ص�ǰ���е�һ�������е���������
	 */
	protected int getResultSize(String tableName) {
		int paralen = Integer.parseInt(SQLmap.get(tableName).get(0));
		return paralen;
	}
	
}