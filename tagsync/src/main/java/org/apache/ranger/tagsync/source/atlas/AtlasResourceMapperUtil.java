/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for th
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.ranger.tagsync.source.atlas;

import org.apache.atlas.typesystem.IReferenceableInstance;
import org.apache.commons.lang.StringUtils;
import org.apache.ranger.plugin.model.RangerServiceResource;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ranger.tagsync.process.TagSyncConfig;

public class AtlasResourceMapperUtil {
	private static final Log LOG = LogFactory.getLog(AtlasResourceMapperUtil.class);

	private static Map<String, AtlasResourceMapper> atlasResourceMappers = new HashMap<String, AtlasResourceMapper>();

	public static boolean isEntityTypeHandled(String entityTypeName) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("==> isEntityTypeHandled(entityTypeName=" + entityTypeName + ")");
		}

		AtlasResourceMapper mapper = atlasResourceMappers.get(entityTypeName);

		boolean ret = mapper != null;

		if (LOG.isDebugEnabled()) {
			LOG.debug("<== isEntityTypeHandled(entityTypeName=" + entityTypeName + ") : " + ret);
		}

		return ret;
	}

	public static RangerServiceResource getRangerServiceResource(IReferenceableInstance atlasEntity) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("==> getRangerServiceResource(" + atlasEntity.getId()._getId() +")");
		}

		RangerServiceResource resource = null;

		AtlasResourceMapper mapper = atlasResourceMappers.get(atlasEntity.getTypeName());

		if (mapper != null) {
			try {
				resource = mapper.buildResource(atlasEntity);
			} catch (Exception exception) {
				LOG.error("Could not get serviceResource for atlas entity:" + atlasEntity.getId()._getId() + ": ", exception);
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("<== getRangerServiceResource(" + atlasEntity.getId()._getId() +"): resource=" + resource);
		}

		return resource;
	}

	static public boolean initializeAtlasResourceMappers(Properties properties) {
		final String MAPPER_NAME_DELIMIER = ",";

		String customMapperNames = TagSyncConfig.getCustomAtlasResourceMappers(properties);

		if (LOG.isDebugEnabled()) {
			LOG.debug("==> initializeAtlasResourceMappers.initializeAtlasResourceMappers(" + customMapperNames + ")");
		}
		boolean ret = true;

		List<String> mapperNames = new ArrayList<String>();
		mapperNames.add("org.apache.ranger.tagsync.source.atlas.AtlasHiveResourceMapper");
		mapperNames.add("org.apache.ranger.tagsync.source.atlas.AtlasHdfsResourceMapper");

		if (StringUtils.isNotBlank(customMapperNames)) {
			for(String customMapperName : customMapperNames.split(MAPPER_NAME_DELIMIER)) {
				mapperNames.add(customMapperName.trim());
			}
		}

		for (String mapperName : mapperNames) {
			try {
				Class clazz = Class.forName(mapperName);
				AtlasResourceMapper resourceMapper = (AtlasResourceMapper) clazz.newInstance();

				resourceMapper.initialize(properties);

				for (String entityTypeName : resourceMapper.getSupportedEntityTypes()) {
					add(entityTypeName, resourceMapper);
				}

			} catch (Exception exception) {
				LOG.error("Failed to create AtlasResourceMapper:" + mapperName + ": ", exception);
				ret = false;
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("<== initializeAtlasResourceMappers.initializeAtlasResourceMappers(" + mapperNames + "): " + ret);
		}
		return ret;
	}

	private static void add(String entityType, AtlasResourceMapper mapper) {
		atlasResourceMappers.put(entityType, mapper);
	}
}
