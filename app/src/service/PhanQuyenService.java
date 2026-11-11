package service;

import entity.Quyen;
import entity.VaiTro;

public class PhanQuyenService {
	public static boolean coQuyen(VaiTro vaiTro, Quyen quyen) {
		if(vaiTro == null || quyen == null) {
			return false;
		}
		switch (vaiTro) {
		case QUAN_LY:
			return true;
			
		case THU_NGAN:
			return quyen == Quyen.DASHBOARD
			|| quyen == Quyen.BAN_HANG
			|| quyen == Quyen.TIM_KIEM_HOA_DON
			|| quyen == Quyen.QUAN_LY_KHACH_HANG;
			
		default:
			return false;
		}
	}
}