package com.tmsps.ne4spring.orm.param;

import java.io.PrintStream;
import java.util.List;

public class NeParamTools {
	public static String handleSql(String sql, NeParamList params) {
		sql = sql.replace("[", "@#").replace("]", "#@");
		StringBuilder sb = new StringBuilder(sql);

		List<NeParam> list = params.getParamList();

		int indexOf = 0;
		for (int index = 0; index < list.size(); index++) {
			NeParam p = (NeParam) list.get(index);
			indexOf = sb.indexOf("?", indexOf);
			if (!p.getIsNull()) {
				indexOf++;
			} else {
				int indexOfL = sb.lastIndexOf("and", indexOf);
				if (indexOfL == -1) {
					indexOfL = sb.lastIndexOf("where", indexOf) + 5 + 1;
				} else {
					indexOfL += 4;
				}

				String conditionL = sb.substring(indexOfL, indexOf);

				int l = containStr(conditionL, '(');
				int r = containStr(conditionL, ')');

				int indexOfR = indexOf + 1;
				for (; r < l; r++) {
					indexOfR = sb.indexOf(")", indexOfR) + 1;
				}

				sb.insert(indexOfL, "[");
				sb.insert(indexOfR + 1, "]");

				indexOf += 3;
			}
		}

		String dkhReg = "\\[[^\\]]*\\]";
		String made = sb.toString().replaceAll(dkhReg + "\\s+(and)", "").replaceAll("(and)\\s+" + dkhReg, "")
				.replaceAll("(where)\\s+" + dkhReg, "");
		made = made.replace("@#", "[").replace("#@", "]");
		return made;
	}

	public static int containStr(String str, char ch) {
		int cnt = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ch) {
				cnt++;
			}
		}
		return cnt;
	}

	public static void main2(String[] args) {
		String sql5 = "select * from t_fk_user t where t.status=0 and (to_days(t.created)=to_days(?)) and (t.name = ?) and ({fn TIMESTAMPDIFF(SQL_TSI_DAY,t.created,?)}=0) order by t.created";
		NeParamList params5 = new NeParamList();
		params5.add("");
		params5.add("x");
		params5.add("x");
		System.err.println("sql5-->" + handleSql(sql5, params5));
	}

	public static void main(String[] args) {
		String sql1 = "select * from t_fk_user t where t.status=0 and (t.uname like ?) and (t.name like ?) and t.user_flag='已认证'";
		NeParamList params1 = new NeParamList();
		params1.add("");
		params1.add("");
		System.err.println(handleSql(sql1, params1));

		String sql2 = "select * from t_fk_user t where (t.uname like ?) and (t.name = ?) and t.user_flag='已认证'";
		NeParamList params2 = new NeParamList();
		params2.add("");
		params2.add("");
		System.err.println(handleSql(sql2, params2));

		String sql3 = "select * from t_fk_user t where (t.uname like ?) and (t.name = ?) order by t.created";
		NeParamList params3 = new NeParamList();
		params3.add("");
		params3.add("");
		System.err.println(handleSql(sql3, params3));

		String sql4 = "select * from t_fk_user t where (t.uname like ?) and t.status=0 and (t.name = ?) order by t.created";
		NeParamList params4 = new NeParamList();
		params4.add("");
		params4.add("");
		System.err.println(handleSql(sql4, params4));

		String sql5 = "select * from t_fk_user t where t.status=0 and (to_days(t.created)=to_days(?)) and (t.name = ?) order by t.created";
		NeParamList params5 = new NeParamList();
		params5.add("");
		params5.add("");
		System.err.println("sql5-->" + handleSql(sql5, params5));

		String sql6 = "select * from t_fk_user t where t.status=0 and (t.id in (?)) and (t.name = ?)";
		NeParamList params6 = new NeParamList();
		params6.add("'1','2','3'");
		params6.add("1");
		System.err.println(handleSql(sql6, params6));

		String sql = "select * from t_fk_user t where t.status=0 and (t.uname like ?) and (t.name = ?)";
		NeParamList params = new NeParamList();
		params.add("");
		params.add("1");
		System.err.println(handleSql(sql, params));
	}
}
