const tourData = [];
const days = 3;

function Tour() {
        this.id = 0;
        this.scheduleId = 0;
        this.title = "";
        this.description = "";
        this.search_priority = "";
        this.start_datetime = "";
        this.arrive_datetime = "";
        this.places = [];
}

function Place() {
        this.id = 0;
        this.tourId = 0;
        this.place_name = "";
        this.place_address = "";
        this.coord_x = 0;
        this.coord_y = 0;
        this.index = 0;
        this.start_datetime = 0;
        this.stay_time = "";
}

for (let i = 0; i < days; i++) {
        const tour = new Tour();
        tour.scheduleId = 71;
        tour.title = `${i + 1}일차`;
        tour.description = `${i + 1}일차`;
        tour.search_priority = "RECOMMEND";

        let startDatetime = new Date("2022-03-31T13:30:00");
        let arriveDatetime = new Date("2022-04-02T15:30:00");
        console.log(startDatetime);
        console.log(arriveDatetime);
        let year = startDatetime.getFullYear();
        let month = startDatetime.getMonth() + 1;
        let startday = startDatetime.getDate() + i;
        let startTime = `${String(startDatetime.getHours()).padStart(2, "0")}:${String(startDatetime.getMinutes()).padStart(2, "0")}:00`;
        let endTime = `${String(arriveDatetime.getHours()).padStart(2, "0")}:${String(arriveDatetime.getMinutes()).padStart(2, "0")}:00`;
        if (i == 0 && days > 1) { // 일정이 여러 날인데 첫 날인 경우
                endTime = `00:00:00`;
        } else if (i == days - 1 && days > 1) { // 일정이 여러 날인데 마지막 날인 경우
                startTime = `00:00:00`;
        } else if (days > 2) {				// 일정이 3일 이상인데 중간에 낀 날인 경우
                startTime = `00:00:00`;
                endTime = `00:00:00`;
        }
        const lastDay = new Date(startDatetime.getFullYear(), startDatetime.getMonth() + 1, 0).getDate();
        let nextDay = lastDay + 1;
        if (startday > new Date(startDatetime.getFullYear(), startDatetime.getMonth() + 1, 0).getDate()) { // 해당 월 말일보다 day가 큰 경우 월++
                month++;
                if (month > 12) { // 월이 12를 초과한 경우 연도 증가
                        month - 12;
                        year++;
                }
                startday -= lastDay;
                nextDay = startday + 1;
        }
        let nextYear = year;
        let nextMonth = month;
        if (nextDay > new Date(startDatetime.getFullYear(), startDatetime.getMonth() + 1, 0).getDate()) {
                nextMonth++;
                if (nextMonth > 12) { // 월이 12를 초과한 경우 연도 증가
                        nextMonth - 12;
                        nextYear++;
                }
                nextDay -= lastDay;
        }
        tour.start_datetime = `${year}-${month}-${startday} ${startTime}`;
        tour.arrive_datetime = `${nextYear}-${nextMonth}-${nextDay} ${endTime}`;

        const place = new Place();
        place.index = 0;
        if (i == 0) {
                place.start_datetime = `${startDatetime.getHours()}:${startDatetime.getMinutes()}:${String(startDatetime.getSeconds()).padStart(2, "0")}`;
        } else {
                place.start_datetime = "00:00:00";
        }
        place.stay_time = "00:00";

        tour.places.push(place);
        tourData.push(tour);

        console.log(tourData);
}