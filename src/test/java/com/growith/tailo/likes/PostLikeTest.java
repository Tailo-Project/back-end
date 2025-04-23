package com.growith.tailo.likes;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class PostLikeTest {

//    @Autowired
//    private PostListService postListService;
//
//    @Autowired
//    private FeedPostRepository feedPostRepository;
//
//    @Autowired
//    private MemberRepository memberRepository;
//
//    @Autowired
//    private PostLikeRepository postLikeRepository;
//
//    @Test
//    @DisplayName("좋아요 동시적 요청이 들어올 때 테스트 - 99번일 때 1")
//    void likeSaveTest() throws InterruptedException {
//        // given
//        Member member = memberRepository.save(Member.builder()
//                .accountId("testUser")
//                .address("서울시 강남구")
//                .age(30)
//                .breed("시베리안 허스키")
//                .email("testuser@example.com")
//                .gender(GenderType.MALE)
//                .nickname("테스트유저")
//                .profileImageUrl("http://example.com/profile.jpg")
//                .role(Role.USER)
//                .type("1")
//                .build());
//
//        FeedPost feedPost = feedPostRepository.save(FeedPost.builder()
//                .content("테스트 피드")
//                .author(member)
//                .build());
//
//        int threadCount = 99;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        // when
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//                    postListService.likeFeedPost(feedPost.getId(), member);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        // then
//        String likeCount = postLikeRepository.countByFeedPostId(feedPost.getId());
//        assertThat(Long.parseLong(likeCount)).isEqualTo(1);
//    }
}
