/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */
let principalId = $("#principalId").val();
let page = 0;

// (1) 스토리 로드하기
function storyLoad() {

	$.ajax({
			type: "get",
			url: `/api/image?page=${page}`,
			dataType: "json"
		}
	).done(res => {
		res.data.content.forEach((image) => {
			let storyItem = getStoryItem(image);
			$("#storyList").append(storyItem);
		})
	}).fail(error => {

	});
}

storyLoad();

function getStoryItem(image) {
	let item = `
				<div class="story-list__item">
					<div class="sl__item__header">
						<div>
							<img class="profile-image" src="/upload/${image.user.profileImageUrl}" onerror="this.src='/images/person.jpeg'" />
						</div>
					<div>${image.user.username}</div>
					</div>

					<div class="sl__item__img">
					<img src="/upload/${image.postImageUrl}" />
				</div>
		
					<div class="sl__item__contents">
						<div class="sl__item__contents__icon">
			
							<button>`;
	if (image.likesState) {
		item += `<i class="fas fa-heart active" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
	} else {
		item += `<i class="far fa-heart " id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
	}

	item += `
							</button>
						</div>
			
						<span class="like"><b id="storyLikeCount-${image.id}">${image.likeCount} </b>likes</span>
			
						<div class="sl__item__contents__content">
							<p>${image.caption}</p>
						</div>
			
						<div id="storyCommentList-${image.id}">`;

	image.commentList.forEach((comment) => {
		item += `<div class="sl__item__contents__comment" id="storyCommentItem-${image.id}">
			<p>
				<b>${comment.user.username} :</b> ${comment.content}
			</p>`
		if ( principalId == comment.user.id) {
			item += `	
			<button onclick="deleteComment(${comment.id})">
				<i class="fas fa-times"></i>
			</button>`;
		}
		item += `</div>`;
	});

	item += `						
						</div>
						<div class="sl__item__input">
							<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />
							<button type="button" onClick="addComment(${image.id})">게시</button>
						</div>
					</div>
				</div>`;
	return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
	// 문서의 높이 : 문서 전체
	let documentHeight = $(document).height();
	// 윈도우의 높이 : 현재 보고 있는 화면의 높이
	let windowHeight = $(window).height();
	// ScrollTop : 변하는 기준
	let scrollHeight = $(window).scrollTop();

	let checkNum = scrollHeight - (documentHeight - windowHeight);

	if (checkNum < 1 && checkNum > -1) {
		page += 1;
		storyLoad();
	}

});


// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
	let likeIcon = $(`#storyLikeIcon-${imageId}`);

	if (likeIcon.hasClass("far")) {

		$.ajax({
			type : "POST",
			url : `/api/image/${imageId}/likes`,
			dataType: "json"
		}).done(res => {
			let sLike = $(`#storyLikeCount-${imageId}`);
			let sLikeCount = sLike.text();
			let likeCount = Number(sLikeCount) + 1;
			sLike.text(likeCount);

			likeIcon.addClass("fas");
			likeIcon.addClass("active");
			likeIcon.removeClass("far");
		}).fail(error => {

		});

	} else {
		$.ajax({
			type : "DELETE",
			url : `/api/image/${imageId}/likes`,
			dataType: "json"
		}).done(res => {
			let sLike = $(`#storyLikeCount-${imageId}`);
			let sLikeCount = sLike.text();
			let likeCount = Number(sLikeCount) - 1;
			sLike.text(likeCount = likeCount < 0 ? 0 : likeCount);

			likeIcon.removeClass("fas");
			likeIcon.removeClass("active");
			likeIcon.addClass("far");
		}).fail(error => {

		});
	}
}

// (4) 댓글쓰기
function addComment(imageId) {

	let commentInput = $(`#storyCommentInput-${imageId}`);
	let commentList = $(`#storyCommentList-${imageId}`);

	let data = {
		imageId : imageId,
		content: commentInput.val()
	}

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	$.ajax({
		type : "post",
		url : `/api/comment`,
		data : JSON.stringify(data),
		contentType : 'application/json; charset=utf-8',
		dataType : 'json'
	}).done(res => {
		console.log("성공", res);
		let comment = res.data;
		let content = `
			  <div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}""> 
			    <p>
					<b>${comment.user.username}:</b>${comment.content}
				</p>
			    <button onclick="deleteComment(${comment.id})"><i class="fas fa-times"></i></button>
			  </div>
		`;
		commentList.prepend(content);


	}).fail(error => {
		console.log("오류", error);
		alert(error.responseJSON.content);
	});


	commentInput.val("");
}

// (5) 댓글 삭제
function deleteComment(commentId) {

	$.ajax({
		type : "delete",
		url : `/api/comment/${commentId}`,
		dataType : "json"
	}).done(res => {
		console.log("성공", res);
		$(`#storyCommentItem-${commentId}`).remove();
	}).fail(error => {
		console.log("오류", error);
	});
}







