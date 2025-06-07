@RestController
@RequestMapping("/api/financial")
@RequiredArgsConstructor
public class FinancialController {

    private final FinancialService financialService;

    // 단일 종목 조회 or 전체 조회
    @GetMapping
    public ApiResponse<?> getFinancial(
            @RequestParam(required = false) String ticker,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(required = false) Integer sentiment
    ) {
        // 단일 종목 조회
        if (ticker != null && !ticker.isBlank()) {
            FinancialDetailResponse detail = financialService.getDetailByTicker(ticker);
            return ApiResponse.success(detail);
        }

        // 전체 리스트 조회 (정렬/필터 적용)
        FinancialListResponse list = financialService.getList(page, size, sortBy, sort, sentiment);
        return ApiResponse.success(list);
    }
}
