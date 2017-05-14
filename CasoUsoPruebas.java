//Este es el archivo donde se va  a implementar el caso de uso de pruebas:
package com.raddarapp.domain.usecase;

import com.karumi.rosie.domain.usecase.RosieUseCase;
import com.karumi.rosie.domain.usecase.annotation.UseCase;
import com.karumi.rosie.repository.PaginatedCollection;
import com.karumi.rosie.repository.datasource.paginated.Page;
import com.karumi.rosie.repository.policy.ReadPolicy;
import com.raddarapp.data.general.general.CommentsRepository;
import com.raddarapp.domain.model.Comment;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;



public class GetCommentsByFootprintId extends RosieUseCase {


    public static final String USE_CASE_GET_COMMENTS_BY_FOOTPRINT_ID = "getCommentsByFootprintId";

    private final CommentsRepository commentsMainRepository;

    @Inject
    public GetCommentsByFootprintId(CommentsRepository commentsMainRepository) {
        this.commentsMainRepository = commentsMainRepository;
    }

    public PaginatedCollection<Comment> getAllCommentsInCache() {
        Collection<Comment> all;

        try {
            all = commentsMainRepository.getAll(ReadPolicy.CACHE_ONLY);
        } catch (Exception e) {
            all = new ArrayList<>();
        }

        if (all == null) {
            all = new ArrayList<>();
        }

        Page page = Page.withOffsetAndLimit(0, all.size());

        PaginatedCollection<Comment> commentsMain = new PaginatedCollection<>(all);
        commentsMain.setPage(page);
        commentsMain.setHasMore(true);

        return commentsMain;
    }

    public void deleteCache() {
        try {
            commentsMainRepository.deleteAll();
        } catch (Exception e) {
        }
    }

    @UseCase(name = USE_CASE_GET_COMMENTS_BY_FOOTPRINT_ID)
    public void getComments(Page page, String footprintKey) throws Exception {
        PaginatedCollection<Comment> commentsMain = commentsMainRepository.getPage(page, ReadPolicy.valueOf(footprintKey));
        notifySuccess(commentsMain);
    }
}
