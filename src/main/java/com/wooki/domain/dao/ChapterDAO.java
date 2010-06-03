//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// 	http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.domain.dao;

import java.util.List;

import com.wooki.domain.model.Chapter;

/**
 * Handle Chapter persistence.
 * 
 * @author ccordenier
 */
public interface ChapterDAO extends GenericDAO<Chapter, Long>
{

    /**
     * Check if the user is author of the parent book of a chapter.
     * 
     * @param chapterId
     * @return
     */
    boolean isAuthor(Long chapterId, String username);

    /**
     * Find previous and next chapter.
     * 
     * @param bookId
     * @param chapterId
     * @return
     */
    List<Object[]> findNext(Long bookId, Long chapterId);

    /**
     * Find previous publish chapter.
     * 
     * @param bookId
     * @param chapterId
     * @return
     */
    List<Object[]> findPrevious(Long bookId, Long chapterId);

    /**
     * List existing chapter for a given book.
     * 
     * @param bookId
     * @return
     */
    List<Chapter> listChapters(Long bookId);

    /**
     * List information chapter, do not load chapter content.
     * 
     * @param bookId
     * @return
     */
    List<Chapter> listChapterInfo(Long bookId);

}
