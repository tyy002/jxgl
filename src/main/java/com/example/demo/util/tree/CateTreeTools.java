package com.example.demo.util.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品分类 树 处理
 * 
 * @author sw
 *
 */
public class CateTreeTools {

	public static void main(String[] args) {

	}

	// 预处理树节点
	public static List<Map<String, Object>> handleTree(List<Map<String, Object>> menuList) {
		return handleTree(menuList, false);
	}

	// 预处理树节点
	public static List<Map<String, Object>> handleTree(List<Map<String, Object>> menuList, boolean isChecked) {
		for (Map<String, Object> map : menuList) {
			map.put("id", map.get("code"));
			map.put("iconCls", map.get("url"));
			map.put("text", map.remove("name"));
			map.put("leaf", true);
			if (isChecked) {
				map.put("checked", false);
			}
		}

		return menuList;
	}

	public static List<Map<String, Object>> turnListToTree(List<Map<String, Object>> menuList) {
		// TODO 转换List为树形结构
		return turnListToTree(menuList, false);
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> turnListToTree(List<Map<String, Object>> menuList, boolean isChecked) {
		// TODO 转换List为树形结构
		menuList = handleTree(menuList, isChecked);

		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> node1 : menuList) {
			String node1_code = (String) node1.get("code");
			String node1_parent_code = node1_code.substring(0, node1_code.length() - 3);

			boolean mark = false;
			for (Map<String, Object> node2 : menuList) {
				String node2_code = (String) node2.get("code");

				if (node1_parent_code != null && node1_parent_code.equals(node2_code)) {
					mark = true;
					if (node2.get("children") == null) {
						node2.put("children", new ArrayList<Map<String, Object>>());
					}
					((List<Map<String, Object>>) node2.get("children")).add(node1);
					node2.put("leaf", false);
					if (!isChecked) {
						node2.put("expanded", false);
					}
					break;
				}
			}
			if (!mark) {
				nodeList.add(node1);
			}
		}
		return nodeList;
	}

}
