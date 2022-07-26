package com.ssafy.runwithme.datasource.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ssafy.runwithme.api.CrewActivityApi
import com.ssafy.runwithme.model.response.CrewBoardResponse
import retrofit2.HttpException
import java.io.IOException

class GetCrewBoardsPagingSource (
    private val crewActivityApi: CrewActivityApi,
    private val crewSeq: Int,
    private val size: Int
): PagingSource<Int, CrewBoardResponse>() {

    // 데이터 로드
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CrewBoardResponse> {
        // LoadParams : 로드할 키와 항목 수 , LoadResult : 로드 작업의 결과
        return try {

            // 최초 요청 페이지
            val position = params.key ?: 0

            // 데이터를 제공하는 인스턴스의 메소드 사용
            val response = crewActivityApi.getCrewBoards(
                crewSeq = crewSeq,
                size = size,
                offset = position
            )

            val nextKey =
                if (response.count < size){
                    null
                }else{
                    position + 1
                }

            /* 로드에 성공 시 LoadResult.Page 반환
            data : 전송되는 데이터
            prevKey : 이전 값 (위 스크롤 방향)
            nextKey : 다음 값 (아래 스크롤 방향)
             */
            LoadResult.Page(
                data = response.data,
                prevKey = null,
                nextKey = nextKey
            )

            // 로드에 실패 시 LoadResult.Error 반환
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    // 데이터가 새로고침되거나 첫 로드 후 무효화되었을 때 키를 반환하여 load()로 전달
    override fun getRefreshKey(state: PagingState<Int, CrewBoardResponse>): Int? {
        TODO("Not yet implemented")
    }
}