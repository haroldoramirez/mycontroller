/*
 * Copyright 2015-2016 Jeeva Kandasamy (jkandasa@gmail.com)
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mycontroller.standalone.api;

import java.util.List;

import org.mycontroller.standalone.api.jaxrs.json.Query;
import org.mycontroller.standalone.api.jaxrs.json.QueryResponse;
import org.mycontroller.standalone.db.DaoUtils;
import org.mycontroller.standalone.db.tables.UidTag;
import org.mycontroller.standalone.exceptions.McBadRequestException;
import org.mycontroller.standalone.exceptions.McDuplicateException;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.3
 */
public class UidTagApi {
    public QueryResponse getAll(Query query) {
        return DaoUtils.getUidTagDao().getAll(query);
    }

    public UidTag get(int id) {
        return DaoUtils.getUidTagDao().getById(id);
    }

    public UidTag getByUid(int uid) {
        return DaoUtils.getUidTagDao().getByUId(uid);
    }

    public UidTag getBySensorVariableId(int sVariableId) {
        return DaoUtils.getUidTagDao().getBySensorVariableId(sVariableId);
    }

    public void delete(List<Integer> ids) {
        DaoUtils.getUidTagDao().deleteByIds(ids);
    }

    public void deleteByUid(Integer uid) {
        DaoUtils.getUidTagDao().deleteByUId(uid);
    }

    public void update(UidTag uidTag) throws McDuplicateException, McBadRequestException {
        UidTag availabilityCheck = DaoUtils.getUidTagDao().getById(uidTag.getId());
        if (availabilityCheck != null) {
            UidTag uidAvailabilityCheck = DaoUtils.getUidTagDao().getByUId(availabilityCheck.getUid());
            if (!uidAvailabilityCheck.getId().equals(availabilityCheck.getId())) {
                throw new McDuplicateException("This UID[" + uidTag.getUid()
                        + "] tagged with another sensor variable["
                        + availabilityCheck.getSensorVariable().toString() + "].");
            }
            UidTag svAvailabilityCheck = DaoUtils.getUidTagDao().getBySensorVariableId(
                    availabilityCheck.getSensorVariable().getId());
            if (!svAvailabilityCheck.getId().equals(availabilityCheck.getId())) {
                throw new McDuplicateException("This sensor variable["
                        + availabilityCheck.getSensorVariable().toString() + "] tagged with another UID["
                        + uidTag.getUid() + "] .");
            }
        }
        DaoUtils.getUidTagDao().update(uidTag);
    }

    public void add(UidTag uidTag) throws McDuplicateException {
        UidTag availabilityCheck = DaoUtils.getUidTagDao().getById(uidTag.getUid());
        if (availabilityCheck != null) {
            throw new McDuplicateException("This UID[" + uidTag.getUid() + "] tagged with another sensor variable["
                    + availabilityCheck.getSensorVariable().toString() + "].");
        }
        availabilityCheck = DaoUtils.getUidTagDao().getBySensorVariableId(uidTag.getSensorVariable().getId());
        if (availabilityCheck != null) {
            throw new McDuplicateException("This sensor variable["
                    + availabilityCheck.getSensorVariable().toString() + "] tagged with another UID["
                    + uidTag.getUid() + "] .");
        }
        DaoUtils.getUidTagDao().create(uidTag);
    }
}
