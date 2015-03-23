package tracer.faultlocalization.core;

import org.apache.log4j.Logger;

public class RankingEdit {
	Logger logger=Logger.getLogger(RankingEdit.class);

	public String edit;
	public double susp_val = 0;
	public int heu_val = 0;

	public int exe_pass_num = 0;
	public int exe_fail_num = 0;

	public RankingEdit(String edit, int heu_val) {
		this.edit = edit;
		this.heu_val = heu_val;
	}

	public RankingEdit(String edit, double susp_val, int heu_val) {
		this.edit = edit;
		this.susp_val = susp_val;
		this.heu_val = heu_val;
	}

	public void increaseExePass() {
		exe_pass_num++;
	}

	public void increaseExeFail() {
		exe_fail_num++;
	}

	public String getType(){
		return edit.substring(0,edit.indexOf(":"));
	}
	
	public String getElement(){
		return edit.substring(edit.indexOf(":")+1);
	}
	
	public String getEdit() {
		return edit;
	}

	public double getSusp_val() {
		return susp_val;
	}

	public int getHeu_val() {
		return heu_val;
	}

	public void getTarantulaVal(int all_pass_num, int all_fail_num) {
		if (all_fail_num == 0)
			susp_val = 0;
		else if (all_pass_num == 0)
			susp_val = 1;
		else {
			susp_val = ((double)exe_fail_num / all_fail_num)
					/ (((double)exe_fail_num / all_fail_num) + ((double)exe_pass_num / all_pass_num));
		}
	}

	public void getSBIVal(int all_pass_num, int all_fail_num) {
		if (all_fail_num == 0)
			susp_val = 0;
		else if (all_pass_num == 0)
			susp_val = 1;
		else {
			susp_val = (double)exe_fail_num / (exe_fail_num + exe_pass_num);
		}
	}

	public void getJaccardVal(int all_pass_num, int all_fail_num) {
		logger.debug("all_pass: "+all_pass_num);
		logger.debug("all_fail: "+all_fail_num);
		logger.debug("exe_pass: "+exe_pass_num);
		logger.debug("exe_fail: "+exe_fail_num);
		if (all_fail_num == 0)
			susp_val = 0;
		else if (all_pass_num == 0)
			susp_val = 1;
		else {
			susp_val = (double)exe_fail_num / (all_fail_num + exe_pass_num);
		}
	}

	public void getOchiaiVal(int all_pass_num, int all_fail_num) {
		if (all_fail_num == 0)
			susp_val = 0;
		else if (all_pass_num == 0)
			susp_val = 1;
		else {
			susp_val = (double)exe_fail_num
					/ Math.sqrt((double)all_fail_num * (exe_fail_num + exe_pass_num));
		}
	}

	public String toString() {
		return edit + " - " + susp_val + " : " + heu_val;
	}
}
