package com.jerboa.datatypes.types

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetModlog(
    val mod_person_id: PersonId? = null,
    val community_id: CommunityId? = null,
    val page: Int? = null,
    val limit: Int? = null,
    val type_: ListingType? /* "All" | "ModRemovePost" | "ModLockPost" | "ModFeaturePost" | "ModRemoveComment" | "ModRemoveCommunity" | "ModBanFromCommunity" | "ModAddCommunity" | "ModTransferCommunity" | "ModAdd" | "ModBan" | "ModHideCommunity" | "AdminPurgePerson" | "AdminPurgeCommunity" | "AdminPurgePost" | "AdminPurgeComment" */ = null,
    val other_person_id: PersonId? = null,
    val auth: String? = null,
) : Parcelable