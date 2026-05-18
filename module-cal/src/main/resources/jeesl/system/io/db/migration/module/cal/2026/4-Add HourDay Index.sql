CREATE INDEX idx_CalDay_year ON CalDay (year_id);
CREATE INDEX idx_CalDay_month ON CalDay (month_id);
CREATE INDEX idx_CalDay_day ON CalDay (day_id);

CREATE INDEX idx_CalHour_day ON CalHour (day_id);
CREATE INDEX idx_CalHour_hour ON CalHour (hour_id);