package com.moondahoon.mobilityclient.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocalDateTimeAdapterTest {

	@Mock
	private JsonWriter jsonWriter;
	@Mock
	private JsonReader jsonReader;

	private LocalDateTimeAdapter adapter;
	private LocalDateTime now;
	private String nowAsString;

	@BeforeEach
	public void setup() {
		adapter = new LocalDateTimeAdapter();
		now = LocalDateTime.now();
		nowAsString = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	@DisplayName("LocalDateTime -> String 테스트")
	@Test
	public void writeTest() throws IOException {
//		given & when
		adapter.write(jsonWriter, now);

//		then
		Mockito.verify(jsonWriter).value(nowAsString);
	}

	@DisplayName("String -> LocalDateTime 테스트")
	@Test
	public void readTest() throws IOException {
//		given & when
		Mockito.when(jsonReader.nextString()).thenReturn(nowAsString);
		LocalDateTime result = adapter.read(jsonReader);

//		then
		assertThat(result).isEqualTo(now.truncatedTo(ChronoUnit.SECONDS));
	}
}