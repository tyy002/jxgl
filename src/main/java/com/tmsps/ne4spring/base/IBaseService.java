package com.tmsps.ne4spring.base;

import com.tmsps.ne4spring.orm.model.DataModel;
import com.tmsps.ne4spring.orm.param.NeParamList;
import com.tmsps.ne4spring.page.Page;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract interface IBaseService {
	public static final Logger log = LoggerFactory.getLogger("Ne4Spring");

	public abstract JdbcTemplate getJdbcTemplate();

	public abstract int saveObj(DataModel paramDataModel);

	public abstract int saveObj(DataModel paramDataModel, boolean paramBoolean);

	public abstract int saveTemplateObj(DataModel paramDataModel);

	public abstract int saveTemplateObj(DataModel paramDataModel, boolean paramBoolean);

	public abstract int save(String paramString, Record paramRecord);

	public abstract void saveObjs(List<DataModel> paramList);

	@Deprecated
	public abstract <T extends DataModel> T findObjById(Object paramObject, Class<? extends DataModel> paramClass);

	public abstract <T extends DataModel> T findById(Object paramObject, Class<? extends DataModel> paramClass);

	public abstract Map<String, Object> findById(String paramString1, String paramString2, String paramString3);

	public abstract Map<String, Object> findById(String paramString1, String paramString2);

	public abstract List<Map<String, Object>> findList(String paramString, Object[] paramArrayOfObject);

	public abstract Map<String, Object> findObj(String paramString, Object[] paramArrayOfObject);

	public abstract Map<String, Object> findObj(String paramString);

	public abstract <T extends DataModel> T findObj(String paramString, Class<? extends DataModel> paramClass);

	public abstract <T extends DataModel> T findForObj(String paramString, Class<? extends DataModel> paramClass);

	public abstract List<Map<String, Object>> findList(String paramString);

	public abstract <T extends DataModel> T findObj(String paramString, Object[] paramArrayOfObject,
			Class<? extends DataModel> paramClass);

	public abstract int deleteObjById(String paramString, Class<? extends DataModel> paramClass);

	public abstract int deleteByID(String paramString1, String paramString2, String paramString3);

	public abstract int updateObj(DataModel paramDataModel);

	public abstract int updateChangeObj(DataModel paramDataModel);

	public abstract int updateTemplateObj(DataModel paramDataModel);

	public abstract List<Map<String, Object>> findList(String paramString1, String paramString2,
			Object[] paramArrayOfObject, Page paramPage);

	public abstract List<Map<String, Object>> findList(String paramString, Object[] paramArrayOfObject,
			Map<String, String> paramMap, Page paramPage);

	public abstract List<Map<String, Object>> findList(String paramString, Object[] paramArrayOfObject, Page paramPage);

	public abstract List<Map<String, Object>> findList(String paramString, Page paramPage);

	public abstract int updObj(Class<?> paramClass, Map<String, Object> paramMap1, Map<String, Object> paramMap2);

	public abstract Map<String, Object> getMap(String paramString, Object paramObject);

	public abstract Map<String, List<Object>> getSearchMap();

	public abstract Map<String, Object> getMap(String[] paramArrayOfString, Object[] paramArrayOfObject);

	public abstract Page pageinate(int paramInt1, int paramInt2, String paramString1, String paramString2,
			Object... paramVarArgs);

	public abstract Page pageinate(int paramInt1, int paramInt2, String paramString1, String paramString2);

	public abstract List<Map<String, Object>> findList(String paramString, NeParamList paramNeParamList,
			Map<String, String> paramMap, Page paramPage);

	public abstract List<Map<String, Object>> findList(String paramString, NeParamList paramNeParamList);
}
