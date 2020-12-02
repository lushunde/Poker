package com.lushunde.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MainRun {

	private static int poker_A = 14;

	public static void main(String[] args) {

		System.out.println("如下规则介绍：");
		System.out.println("	两个字符代表一张牌，第一个字符SHDC代表花色：黑红方草，第二个字符2-9或者TJQKA代表大小。");
		System.out.println("	输入20个字符，前十个代表第一手牌，后10个代表第二手牌。例如 S2S3S4S5S7D4D9DAD2C4 ");
		System.out.println("	输入 小于100以内的整数，则自动生成两手拍比较（偷懒）");
		System.out.println("	输出 > 代表第一手牌大于第二手牌， < 代表小于， = 代表平。");

		System.out.println("开始输入...");

		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {

			String line = sc.next();
			if(line != null){
				line = line.trim();
			}
			
			if (line == null || line.trim().equals("")) {
				
			} else if (line.equalsIgnoreCase("exit")) {
				System.out.println("退出系统");
				break;
			} else if(line.matches("[123456789][0123456789]?")){
				Integer forNum = Integer.valueOf(line);
				for (int i = 0; i < forNum; i++) {
					line = getAutoPokerString();
					System.out.println("自动随机输入两手牌："+ line);
					execute(line);
				}
			}else if (line.length() == 20) {
				execute(line);
				
			} else {
				System.out.println();
				System.out.println("输入错误，请重新输入...");
				
			}
		}

		sc.close();

	}


	private static void execute(String line) {
		List<Poker> first = new ArrayList<>();
		List<Poker> last = new ArrayList<>();
		conver(first, last, line);

		
		// 比较
		comparePoker(first, last);

		sleep(1000l);
		
		// 提示
		System.out.println();
		System.out.println("请输入下一次比较的数据...");
	}

	
	/**
	 * 偷懒自动生产两手拍
	 * @return
	 */
	private static String getAutoPokerString() {
		
		String  input = "";
		
		String[] decors  = {"S","H","D","C"};
		String[] values  = {"2","3","4","5","6","7","8","9","T","J","Q","K","A"};
		
		ArrayList<String> pokers = new ArrayList<>();
		Random rand = new Random();
		while (pokers.size()<10) {
			String poker = decors[rand.nextInt(4)] + values[rand.nextInt(13)];	
			if(!pokers.contains(poker)){
				pokers.add(poker);
			}
		}
		
		for (String poker : pokers) {
			input = input + poker;
		}
		return input;
	}

	

	private static void sleep(long l) {
		try {
			Thread.sleep(l);
		} catch (InterruptedException e) {
			
		}
	}

	/**
	 * 两手牌比较
	 * 
	 * @param first
	 * @param last
	 */

	private static void comparePoker(List<Poker> first, List<Poker> last) {
		
		System.out.println("正在比较中... 请稍等...");
		sleep(500l);
		
		
		int firstType = getPokerByType(first);
		int lastType = getPokerByType(last);

		// 完成排序
		System.out.println("第一手牌：" + getType(firstType) + first);
		System.out.println("第二手牌：" + getType(lastType)  + last);
		
		int compare = Integer.compare(firstType, lastType);
		if(compare==0){
			compare = compareType(firstType,first,last);
		}
		
		System.out.print("比较结果：");
		if(compare == 0){
			System.out.println("  =  ");
		}else if (compare > 0) {
			System.out.println("  >  ");
		}else if (compare < 0) {
			System.out.println("  <  ");
		}
	}

	private static int POYALFLUSH = 9; // 皇家同花顺
	private static int STRAIGHTFLUSH = 8;// 同花顺
	private static int FOUROFAKING = 7; // 四条
	private static int FULLHOUSE = 6; // 葫芦
	private static int FLUSH = 5; // 同花
	private static int STRAIGHT = 4; // 顺子
	private static int THREEOFAKIND = 3; // 三条
	private static int TWOPAIR = 2; // 两对
	private static int ONEPAIR = 1; // 一对
	private static int HIGHCARD = 0; // 高牌
	
	
	/**
	 * 返回牌类型
	 * @return
	 */
	private static String getType(Integer type) {
		if(POYALFLUSH==type){
			return "皇家同花顺";
		}else if( STRAIGHTFLUSH == type){
			return "同花顺";
		}else if( FOUROFAKING == type){
			return "四条";
		}else if( FULLHOUSE == type){
			return "葫芦";
		}else if( FLUSH == type){
			return "同花";
		}else if( STRAIGHT == type){
			return "顺子";
		}else if( THREEOFAKIND == type){
			return "三条";
		}else if( TWOPAIR == type){
			return "两对";
		}else if( ONEPAIR == type){
			return "一对";
		}else if( HIGHCARD == type){
			return "高牌";
		}else {
			throw new RuntimeException("解析的牌类型不对");
		}
		
		
	}

	/**
	 * 相同类型 大小比较
	 * @param firstType
	 * @param first
	 * @param last
	 * @return
	 */
	private static int compareType(int type, List<Poker> first, List<Poker> last) {
//		if(POYALFLUSH== type || STRAIGHTFLUSH== type || STRAIGHT == type){
//			// 相同类型，顺子第一位既可以比较大小
//			return Integer.compare(first.get(0).getValue(),last.get(0).getValue());
//		}else if(FOUROFAKING == type ||FULLHOUSE==type ){
			//包含四条、三条、对子
			 ArrayList<Integer> firstOrder = getPokerMapOrder(first);
			 ArrayList<Integer> lastOrder = getPokerMapOrder(last);
			 return comparePokerNum(firstOrder, lastOrder);
//		}else if(FLUSH == type || HIGHCARD == type ){
//			// 同花 或者 高牌，直接比较大小
//			return comparePokerNum2(first, last);
//			
//		}
		
	}

	/**
	 * 三条、两对、一对  拍打小排序
	 * @param first
	 * @return
	 */
	private static ArrayList<Integer> getPokerMapOrder(List<Poker> list) {
		ArrayList<Integer> order = new ArrayList<>();
		Map<Integer, Integer> check = getPairMap(list);
		list.forEach((poker) -> {
			// 取三条
			if(check.get(poker.getValue())==4){
				order.add(poker.getValue());
			}
		});
		list.forEach((poker) -> {
			// 取三条
			if(check.get(poker.getValue())==3){
				order.add(poker.getValue());
			}
		});
		list.forEach((poker) -> {
			// 取二条
			if(check.get(poker.getValue())==2){
				order.add(poker.getValue());
			}
		});
		list.forEach((poker) -> {
			// 取二条
			if(check.get(poker.getValue())==1){
				order.add(poker.getValue());
			}
		});
		
		return order;
	}

//	/**
//	 * 直接比较大小
//	 * @param first
//	 * @param last
//	 * @return
//	 */
//	private static int comparePokerNum2(List<Poker> first, List<Poker> last) {
//		int compare = 0;
//		for (int i = 0; i < first.size(); i++) {
//			if(compare==0){
//				compare = Integer.compare(first.get(i).getValue(), last.get(i).getValue());
//			}else{
//				break;
//			}
//			
//		}
//		return compare;
//	}

	/**
	 * 顺序比较牌的大小
	 * @param firstOrder
	 * @param lastOrder
	 */
	private static Integer comparePokerNum(ArrayList<Integer> firstOrder, ArrayList<Integer> lastOrder) {
		int compare = 0;
		for (int i = 0; i < firstOrder.size(); i++) {
			if(compare==0){
				compare = Integer.compare(firstOrder.get(i), lastOrder.get(i));
			}else{
				break;
			}
			
		}
		return compare;
	}

//	/**
//	 * 获取一个 按照 对比大小顺序返回map
//	 * @param first
//	 * @return
//	 */
//	private static ArrayList<Integer> getPokerMapOrder(List<Poker> list) {
//		
//		ArrayList<Integer> order = new ArrayList<>();
//		Map<Integer, Integer> check = getPairMap(list);
//		
//		if(check.containsKey(4)){
//			order.add(check.get(4));
//		}else if(check.containsKey(3)){
//			order.add(check.get(3));
//		}else if(check.containsKey(2)){
//			order.add(check.get(2));
//		}else if(check.containsKey(1)){
//			order.add(check.get(1));
//		}else{
//			throw new RuntimeException("对子算法错误，请检查");
//		}
//		
//		
//		return order;
//	}

	// 类型
	


	
	
	
	private static int getPokerByType(List<Poker> list) {

		if (checkFlush(list) && checkStraight(list)) {
			if (list.get(0).getValue() == poker_A) {

				return POYALFLUSH; // 皇家同花顺
			} else {
				return STRAIGHTFLUSH; // 同花顺
			}
		}
		if (checkFlush(list) && !checkStraight(list)) {
			// 同花
			return FLUSH; // 同花

		} else if (!checkFlush(list) && checkStraight(list)) {
			return STRAIGHT; // 顺子
		} else if (checkFourOfaKind(list)) {
			return FOUROFAKING; // 四条
		} else if (checkFullHouse(list)) {
			return FULLHOUSE;// 葫芦
		} else if(checkThreeOfAKind(list)){
			return THREEOFAKIND; // 三条
		}else if(checkTwoPair(list)){
			return TWOPAIR ; // 两对
		}else if(checkOnePair(list)){
			return ONEPAIR ; // 一对
		}else if(checkHighCard(list)){
			return HIGHCARD ;  // 高牌
		}else{
			
			throw new RuntimeException("算法错误，请检查");
		}

	}

	/**
	 * 校验是否是高牌
	 * @param list
	 * @return
	 */
	private static boolean checkHighCard(List<Poker> list) {
		Map<Integer, Integer> check = getPairMap(list);
		if (check.size()==5) {
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 校验是否是一对
	 * @param list
	 * @return
	 */
	private static boolean checkOnePair(List<Poker> list) {
		Map<Integer, Integer> check = getPairMap(list);
		if (check.size()==4) {
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 检查是否是两对
	 * @param list
	 * @return
	 */
	private static boolean checkTwoPair(List<Poker> list) {
		Map<Integer, Integer> check = getPairMap(list);
		
		if (check.size()==3) {
			return true;
		}else{
			return false;
		}
	}

	private static Map<Integer, Integer> getPairMap(List<Poker> list) {
		Map<Integer,Integer> check = new HashMap<Integer,Integer>();
		for (Poker poker : list) {
			Integer value = poker.getValue();
			if(check.containsKey(value)){
				check.put(value, check.get(value)+1);
			}else{
				check.put(value, 1);
			}
		}
		return check;
	}

	/**
	 * 校验是否是三条
	 * @param list
	 * @return
	 */
	private static boolean checkThreeOfAKind(List<Poker> list) {
		if ((list.get(2).getValue() == list.get(1).getValue() && list.get(2).getValue() == list.get(0).getValue()) ||
				 (list.get(2).getValue() == list.get(3).getValue()
						&& list.get(2).getValue() == list.get(4).getValue())
		) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查是否是 葫芦
	 * @param list
	 * @return
	 */
	private static boolean checkFullHouse(List<Poker> list) {
		if (list.get(0).getValue() == list.get(1).getValue() && list.get(3).getValue() == list.get(4).getValue()
				&& (list.get(0).getValue() == list.get(2).getValue()
						|| list.get(3).getValue() == list.get(2).getValue())

		) {
			return true;
		} else {
			return false;
		}
	}

	// 校验是否是 四条
	private static boolean checkFourOfaKind(List<Poker> list) {
		if ((list.get(1).getValue() == list.get(2).getValue() && list.get(1).getValue() == list.get(3).getValue())
				&& (list.get(1).getValue() == list.get(0).getValue()
						|| list.get(1).getValue() == list.get(4).getValue())

		) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是顺子
	 * 
	 * @param list
	 * @return
	 */
	private static boolean checkStraight(List<Poker> list) {
		if (list.get(0).getValue() == list.get(1).getValue() + 1 && list.get(0).getValue() == list.get(2).getValue() + 2
				&& list.get(0).getValue() == list.get(3).getValue() + 2
				&& list.get(0).getValue() == list.get(4).getValue() + 4) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检查手牌是否是同花
	 * 
	 * @param list
	 * @return
	 */
	private static boolean checkFlush(List<Poker> list) {
		if (list.get(0).getDecor() == list.get(1).getDecor() && list.get(0).getDecor() == list.get(2).getDecor()
				&& list.get(0).getDecor() == list.get(3).getDecor()
				&& list.get(0).getDecor() == list.get(4).getDecor()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将line转换为 牌
	 * 
	 * @param first
	 * @param last
	 * @param line
	 */
	private static void conver(List<Poker> first, List<Poker> last, String line) {
		// TODO Auto-generated method stub

		for (int i = 0; i < 10; i++) {
			String poker = line.substring(i * 2, i * 2 + 2);
			if (!poker.matches("[SHDC].")) {
				System.out.println("输入的第" + i + "张牌[" + poker + "]花色错误，请重新输入...");
				return;
			}
			if (!poker.matches(".[23456789TJQKA]")) {
				System.out.println("输入的第" + i + "张牌[" + poker + "]数字错误，请重新输入...");
				return;
			}

			// 添加
			if (first.size() < 5) {
				if (first.contains(poker)) {
					System.out.println("输入的第" + i + "张牌[" + poker + "]重复，请重新输入...");
					return;
				} else {
					first.add(new Poker(poker));
				}
			} else {
				if (first.contains(poker) || last.contains(poker)) {
					System.out.println("输入的第" + i + "张牌[" + poker + "]重复，请重新输入...");
					return;
				} else {
					last.add(new Poker(poker));
				}
			}

		}

		// 排序
		Collections.sort(first);
		Collections.sort(last);

	}

	
	
}
