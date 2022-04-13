const searcher = document.querySelector(".searcher");
const searcher_placeholder = document.querySelector(".searcher-placeholder");
const searcher_input = document.querySelector(".searcher > input");
const result_wrapper = document.querySelector(".result-wrapper");

let prevKeyword = "";

searcher_placeholder.onclick = focusSearchInput;
searcher_input.onblur = blurSearchInput;

function focusSearchInput(event) {
        if (prevKeyword != null || prevKeyword != "") {
                searcher_input.value = prevKeyword;
        }
        const eraseButton = makeEraseKeywordButton();
        const result_box = makeResultBox();

        searcher_input.focus();
        searcher.insertBefore(eraseButton, result_wrapper);
        result_wrapper.appendChild(result_box);
        event.target.classList.add("hidden");
}

function blurSearchInput(event) {
        const eraseButton = document.querySelector(".erase-keyword");
        if (event.relatedTarget == null || event.relatedTarget != eraseButton) {
                prevKeyword = searcher_input.value;
                if ((prevKeyword != null || prevKeyword != "") && searcher_input.value != "") {
                        searcher_placeholder.querySelector(".placeholder-text").innerText = prevKeyword;
                }
        } else {
                prevKeyword = "";
                searcher_placeholder.querySelector(".placeholder-text").innerText = "검색";
        }
        eraseButton.remove();
        searcher_placeholder.classList.remove("hidden");
        result_wrapper.querySelector(".result-box").remove();
}

function makeEraseKeywordButton() {
        const button = document.createElement("button");
        button.type = "button";
        button.className = "erase-keyword";
        return button;
}

function makeResultBox() {
        const result_box = document.createElement("div");
        result_box.className = "result-box-to-hide result-box";
        result_box.innerHTML = `<div class="arrow"></div>
                                                <div class="result-lists">
                                                        <div class="result-header">
                                                                <span>최근 검색 항목</span>
                                                        </div>
                                                        <div class="results">
                                                                <div>최근 검색 내역 없음.</div>
                                                        </div>
                                                </div>`;
        return result_box;
}