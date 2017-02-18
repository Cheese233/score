package com.prmol.score;

public enum duanwei {
	bj5, bj4, bj3, bj2, bj1, zs5, zs4, zs3, zs2, zs1, cfds, zqwz;
	public static int getLevel(duanwei dw) {
		switch (dw) {
		default:
			return 0;
		case bj5:
			return 1;
		case bj4:
			return 2;
		case bj3:
			return 3;
		case bj2:
			return 4;
		case bj1:
			return 5;
		case zs5:
			return 6;
		case zs4:
			return 7;
		case zs3:
			return 8;
		case zs2:
			return 9;
		case zs1:
			return 10;
		case cfds:
			return 11;
		case zqwz:
			return 12;
		}
	}

	public static duanwei getduanwei(int score) {
		if (score >= 120000) {
			return bj5;
		} else if (score >= 150000) {
			return bj4;
		} else if (score >= 200000) {
			return bj3;
		} else if (score >= 300000) {
			return bj2;
		} else if (score >= 500000) {
			return bj1;
		} else if (score >= 700000) {
			return zs5;
		} else if (score >= 900000) {
			return zs4;
		} else if (score >= 1200000) {
			return zs3;
		} else if (score >= 1500000) {
			return zs2;
		} else if (score >= 2000000) {
			return zs1;
		} else if (score >= 3200000) {
			return cfds;
		} else if (score >= 6666666) {
			return zqwz;
		}
		return duanwei.bj1;
	}
}
