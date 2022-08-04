package com.ssafy.runwithme.datasource.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ssafy.runwithme.api.MyActivityApi
import com.ssafy.runwithme.model.dto.CrewBoardDto
import retrofit2.HttpException
import java.io.IOException

class GetMyBoardsPagingSource (
    private val myActivityApi: MyActivityApi,
    private val size: Int
): PagingSource<Int, CrewBoardDto>() {

    // 데이터 로드
    override suspend fun load(params: LoadParams<Int>): PagingSource.LoadResult<Int, CrewBoardDto> {
        // LoadParams : 로드할 키와 항목 수 , LoadResult : 로드 작업의 결과
        return try {

            // 최초 요청 페이지
            val position = params.key ?: 0

            // 데이터를 제공하는 인스턴스의 메소드 사용
            val response = myActivityApi.getMyBoards(
                size = size,
                boardMaxSeq = position
            )
            Log.d("test5", "load: $response")

            val nextKey =
                // 사이즈 보다 불러온 것이 작으면 더 불러올 필요가 없다.
                if (response.count < size){
                    null
                }
                // 더 불러 올 때 마지막 인덱스 값을 주어 그 이후의 값들을 불러온다.
                else{
                    response.data.last().crewBoardSeq
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
    override fun getRefreshKey(state: PagingState<Int, CrewBoardDto>): Int? {
        TODO("Not yet implemented")
    }
}