package com.lushunde.poker;

public class Poker implements Comparable<Poker> {

	private String poker ;  // 牌
	private Integer decor ;  // 花色
	private Integer value ; // 数值
	
	
	public Poker(String poker) {
		super();
		this.poker = poker;
		this.decor = getDecorInt(poker.substring(0,1));
		this.value = getValueInt(poker.substring(1,2));
	}
	
	//SHDC代表花色：黑红方草
	private Integer getDecorInt(String num) {
		 if(num.equals("S")){
			return 4;
		}else if(num.equals("H")){
			
			return 3;
		}else if(num.equals("D")){
			
			return 2;
		}else if(num.equals("C")){
			
			return 1;
		
		}else{
			throw new RuntimeException("数据不正确");
		}
	}


	private Integer getValueInt(String num) {
		if(num.matches("[23456789]")){
			return Integer.valueOf(num);
		}else if(num.equals("T")){
			return 10;
		}else if(num.equals("J")){
			
			return 11;
		}else if(num.equals("Q")){
			
			return 12;
		}else if(num.equals("K")){
			
			return 13;
		}else if(num.equals("A")){
			return 14;
		}else{
			throw new RuntimeException("数据不正确");
		}
	}
	public String getPoker() {
		return poker;
	}
	public void setPoker(String poker) {
		this.poker = poker;
	}
	


	public Integer getDecor() {
		return decor;
	}

	

	public Integer getValue() {
		return value;
	}



//	@Override
//	public int compare(Poke o1, Poke o2) {
//		return (o1.getValue().compareTo(o2.getValue())!=0)? (o1.getValue().compareTo(o2.getValue())): (o1.getDecor().compareTo(o2.getDecor())) ;
//	}

	@Override
	public String toString() {
		return poker + "-" + getPokerDesc() ;
	}

	private String getPokerDesc() {
			if (poker.endsWith("T")) {
				poker = poker.replace("T", "10");
			}
			if (poker.startsWith("S")) {
				return poker.replace("S", "♠");
			} else if (poker.startsWith("H")) {
				return poker.replace("H", "♥");
			} else if (poker.startsWith("C")) {
				return poker.replace("C", "♣");
			} else if (poker.startsWith("D")) {
				return poker.replace("D", "♦");
			} else {
				return poker;
			}
	}

	@Override
	public int compareTo(Poker o) {
		return (o.getValue().compareTo(this.getValue())!=0)? (o.getValue().compareTo(this.getValue())): (o.getDecor().compareTo(this.getDecor())) ;
		
	}
	
	
	
	
}
