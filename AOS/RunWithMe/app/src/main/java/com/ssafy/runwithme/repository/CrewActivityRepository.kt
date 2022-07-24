package com.ssafy.runwithme.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ssafy.runwithme.api.CrewActivityApi
import com.ssafy.runwithme.datasource.paging.GetCrewBoardsPagingSource
import javax.inject.Inject

class CrewActivityRepository @Inject constructor(
    private val crewActivityApi: CrewActivityApi,
) {
    fun getCrewBoards(crewId: Int) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GetCrewBoardsPagingSource(crewActivityApi = crewActivityApi, crewId = crewId)}
        ).flow
}