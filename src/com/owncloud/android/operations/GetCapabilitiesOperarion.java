/**
 *   ownCloud Android client application
 *
 *   @author masensio
 *   Copyright (C) 2015 ownCloud Inc.
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License version 2,
 *   as published by the Free Software Foundation.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.owncloud.android.operations;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.status.GetRemoteCapabilitiesOperation;
import com.owncloud.android.operations.common.SyncOperation;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Get and save capabilities from the server
 */
public class GetCapabilitiesOperarion extends SyncOperation {

    @Override
    protected RemoteOperationResult run(OwnCloudClient client) {
        GetRemoteCapabilitiesOperation getCapabilities = new GetRemoteCapabilitiesOperation();
        RemoteOperationResult result = getCapabilities.execute(client);

        if (result.isSuccess()){
            // Read data from the result
            ArrayList<Object> data = result.getData();
            // Version
            Hashtable<String, String> versionTable = (Hashtable<String,String>) data.get(0);
            ServerVersion version = new ServerVersion();
            version.mMayor = Integer.getInteger(versionTable.get(GetRemoteCapabilitiesOperation.PROPERTY_MAJOR));
            version.mMinor = Integer.getInteger(versionTable.get(GetRemoteCapabilitiesOperation.PROPERTY_MINOR));
            version.mMicro = Integer.getInteger(versionTable.get(GetRemoteCapabilitiesOperation.PROPERTY_MICRO));
            version.mString = versionTable.get(GetRemoteCapabilitiesOperation.PROPERTY_STRING);
            version.mEdition = versionTable.get(GetRemoteCapabilitiesOperation.PROPERTY_EDITION);

            // Core
            int corePollinterval = (int) data.get(1);

            // File_Sharing
            Hashtable<Boolean, String> filesSharingTable = (Hashtable<Boolean,String>) data.get(2);
            FilesSharing filesSharing = new FilesSharing();
            filesSharing.mPublicEnabled = Boolean.valueOf(filesSharingTable.get(
                    GetRemoteCapabilitiesOperation.NODE_PUBLIC + ":" +
                            GetRemoteCapabilitiesOperation.PROPERTY_ENABLED));
            filesSharing.mPublicPasswordEnforced = Boolean.valueOf(filesSharingTable.get(
                    GetRemoteCapabilitiesOperation.NODE_PUBLIC + "-" +
                            GetRemoteCapabilitiesOperation.NODE_PASSWORD + ":" +
                            GetRemoteCapabilitiesOperation.PROPERTY_ENFORCED));
            filesSharing.mPublicExpireDateEnabled = Boolean.valueOf(filesSharingTable.get(
                    GetRemoteCapabilitiesOperation.NODE_PUBLIC + "-" +
                            GetRemoteCapabilitiesOperation.NODE_EXPIRE_DATE + ":" +
                            GetRemoteCapabilitiesOperation.PROPERTY_ENABLED));
            filesSharing.mPublicSendMail = Boolean.valueOf(filesSharingTable.get(
                    GetRemoteCapabilitiesOperation.NODE_PUBLIC + ":" +
                            GetRemoteCapabilitiesOperation.PROPERTY_SEND_MAIL));
            filesSharing.mPublicUpload = Boolean.valueOf(filesSharingTable.get(
                    GetRemoteCapabilitiesOperation.NODE_PUBLIC + ":" +
                            GetRemoteCapabilitiesOperation.PROPERTY_UPLOAD));
            filesSharing.mUserSendMail = Boolean.valueOf(filesSharingTable.get(
                    GetRemoteCapabilitiesOperation.NODE_USER + ":" +
                            GetRemoteCapabilitiesOperation.PROPERTY_SEND_MAIL));
            filesSharing.mResharing = Boolean.valueOf(filesSharingTable.get(
                    GetRemoteCapabilitiesOperation.PROPERTY_RESHARING ));

            // Files
            Hashtable<Boolean, String> filesTable = (Hashtable<Boolean,String>) data.get(3);
            Files files = new Files();
            files.mBigFileChuncking = Boolean.valueOf(filesTable.get(
                    GetRemoteCapabilitiesOperation.PROPERTY_BIGFILECHUNKING ));
            files.mUndelete = Boolean.valueOf(filesTable.get(
                    GetRemoteCapabilitiesOperation.PROPERTY_UNDELETE ));
            files.mVersioning = Boolean.valueOf(filesTable.get(
                    GetRemoteCapabilitiesOperation.PROPERTY_VERSIONING ));

            // TODO: Save the capabilities into database
            //getStorageManager().saveCapabilities(version, corePollinterval, filesSharing, files);
        }

        return result;
    }

    // Static class for save capabilities on Database
    public static class ServerVersion {
        public int mMayor = 0;
        public int mMinor = 0;
        public int mMicro = 0;
        public String mString = "";
        public String mEdition = "";
    }

    public static class FilesSharing {
        public boolean mPublicEnabled;
        public boolean mPublicPasswordEnforced;
        public boolean mPublicExpireDateEnabled;
        public boolean mPublicSendMail;
        public boolean mPublicUpload;
        public boolean mUserSendMail;
        public boolean mResharing;
    }

    public static class Files {
        public boolean mBigFileChuncking;
        public boolean mUndelete;
        public boolean mVersioning;
    }
}
