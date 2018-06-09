package com.zhlzzz.together.article.discuss;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussCriteria {

    private Boolean audit;

    private Long articleId;

    private Long userId;

}
