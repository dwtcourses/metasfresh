-- 06.11.2015 11:40
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_RelationType (AD_Client_ID,AD_Org_ID,AD_RelationType_ID,Created,CreatedBy,EntityType,IsActive,IsDirected,IsExplicit,Name,Updated,UpdatedBy) VALUES (0,0,540137,TO_TIMESTAMP('2015-11-06 11:40:52','YYYY-MM-DD HH24:MI:SS'),100,'de.metas.materialtracking','Y','N','N','M_InOut -> Material_Tracking',TO_TIMESTAMP('2015-11-06 11:40:52','YYYY-MM-DD HH24:MI:SS'),100)
;

-- 06.11.2015 11:42
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_Reference (AD_Client_ID,AD_Org_ID,AD_Reference_ID,Created,CreatedBy,EntityType,IsActive,IsOrderByValue,Name,Updated,UpdatedBy,ValidationType) VALUES (0,0,540600,TO_TIMESTAMP('2015-11-06 11:42:23','YYYY-MM-DD HH24:MI:SS'),100,'de.metas.materialtracking','Y','N','M_InOut -> M_Material_Tracking',TO_TIMESTAMP('2015-11-06 11:42:23','YYYY-MM-DD HH24:MI:SS'),100,'T')
;

-- 06.11.2015 11:42
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_Reference_Trl (AD_Language,AD_Reference_ID, Description,Help,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language,t.AD_Reference_ID, t.Description,t.Help,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Reference t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Reference_ID=540600 AND NOT EXISTS (SELECT * FROM AD_Reference_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Reference_ID=t.AD_Reference_ID)
;

-- 06.11.2015 11:42
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_Ref_Table (AD_Client_ID,AD_Key,AD_Org_ID,AD_Reference_ID,AD_Table_ID,Created,CreatedBy,EntityType,IsActive,IsValueDisplayed,Updated,UpdatedBy) VALUES (0,3521,0,540600,319,TO_TIMESTAMP('2015-11-06 11:42:52','YYYY-MM-DD HH24:MI:SS'),100,'de.metas.materialtracking','Y','N',TO_TIMESTAMP('2015-11-06 11:42:52','YYYY-MM-DD HH24:MI:SS'),100)
;

-- 06.11.2015 11:46
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
UPDATE AD_Ref_Table SET AD_Window_ID=184, WhereClause='

exists
(
	select 1
	from M_InOut io
	join M_InOutLine iol on io.M_InOut_ID = iol.M_InOut_ID and io.isActive = ''Y'' and iol.isactive = ''Y''
	join M_Material_Tracking_Ref mtr on  iol.M_InOutLine_ID = mtr.Record_ID and  mtr.AD_Table_ID = ( select ad_Table_ID from AD_Table where tablename = ''M_InOutLine'') and mtr.isActive = ''Y''
	join M_Material_Tracking mt on mtr.M_Material_Tracking_ID = mt.M_Material_Tracking_ID and mt.isActive = ''Y''
	where
		M_InOut.M_InOut_ID = io.M_InOut_ID and
		( io.M_InOut_ID = @M_InOut_ID/-1@ or mt.M_Material_Tracking_ID = @M_Material_Tracking_ID/-1@)
)

',Updated=TO_TIMESTAMP('2015-11-06 11:46:50','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_Reference_ID=540600
;

-- 06.11.2015 11:47
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
UPDATE AD_RelationType SET AD_Reference_Source_ID=540600,Updated=TO_TIMESTAMP('2015-11-06 11:47:09','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_RelationType_ID=540137
;

-- 06.11.2015 11:47
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_Reference (AD_Client_ID,AD_Org_ID,AD_Reference_ID,Created,CreatedBy,EntityType,IsActive,IsOrderByValue,Name,Updated,UpdatedBy,ValidationType) VALUES (0,0,540601,TO_TIMESTAMP('2015-11-06 11:47:49','YYYY-MM-DD HH24:MI:SS'),100,'de.metas.materialtracking','Y','N','RelType M_InOut -> M_Material_Tracking',TO_TIMESTAMP('2015-11-06 11:47:49','YYYY-MM-DD HH24:MI:SS'),100,'T')
;

-- 06.11.2015 11:47
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_Reference_Trl (AD_Language,AD_Reference_ID, Description,Help,Name, IsTranslated,AD_Client_ID,AD_Org_ID,Created,Createdby,Updated,UpdatedBy) SELECT l.AD_Language,t.AD_Reference_ID, t.Description,t.Help,t.Name, 'N',t.AD_Client_ID,t.AD_Org_ID,t.Created,t.Createdby,t.Updated,t.UpdatedBy FROM AD_Language l, AD_Reference t WHERE l.IsActive='Y' AND l.IsSystemLanguage='Y' AND l.IsBaseLanguage='N' AND t.AD_Reference_ID=540601 AND NOT EXISTS (SELECT * FROM AD_Reference_Trl tt WHERE tt.AD_Language=l.AD_Language AND tt.AD_Reference_ID=t.AD_Reference_ID)
;

-- 06.11.2015 11:50
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
INSERT INTO AD_Ref_Table (AD_Client_ID,AD_Key,AD_Org_ID,AD_Reference_ID,AD_Table_ID,AD_Window_ID,Created,CreatedBy,EntityType,IsActive,IsValueDisplayed,Updated,UpdatedBy,WhereClause) VALUES (0,551110,0,540601,540610,540226,TO_TIMESTAMP('2015-11-06 11:50:30','YYYY-MM-DD HH24:MI:SS'),100,'de.metas.materialtracking','Y','N',TO_TIMESTAMP('2015-11-06 11:50:30','YYYY-MM-DD HH24:MI:SS'),100,'

exists
(
	select 1
	from M_InOut io
	join M_InOutLine iol on io.M_InOut_ID = iol.M_InOut_ID and io.isActive = ''Y'' and iol.isactive = ''Y''
	join M_Material_Tracking_Ref mtr on  iol.M_InOutLine_ID = mtr.Record_ID and  mtr.AD_Table_ID = ( select ad_Table_ID from AD_Table where tablename = ''M_InOutLine'') and mtr.isActive = ''Y''
	join M_Material_Tracking mt on mtr.M_Material_Tracking_ID = mt.M_Material_Tracking_ID and mt.isActive = ''Y''
	where
		M_Material_Tracking.M_Material_Tracking_ID = mt.M_Material_Tracking_ID and
		( io.M_InOut_ID = @M_InOut_ID/-1@ or mt.M_Material_Tracking_ID = @M_Material_Tracking_ID/-1@)
)
')
;

-- 06.11.2015 11:50
-- I forgot to set the DICTIONARY_ID_COMMENTS System Configurator
UPDATE AD_RelationType SET AD_Reference_Target_ID=540601,Updated=TO_TIMESTAMP('2015-11-06 11:50:44','YYYY-MM-DD HH24:MI:SS'),UpdatedBy=100 WHERE AD_RelationType_ID=540137
;

