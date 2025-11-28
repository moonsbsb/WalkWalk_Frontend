package com.withwalk.app.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.withwalk.app.ui.theme.dark_grey
import com.withwalk.app.ui.theme.middle_grey
import com.withwalk.app.ui.theme.white
import com.withwalk.app.R
import com.withwalk.app.ui.theme.grey

@Composable
fun GuideScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().background(color = white).padding(20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.finish),
            contentDescription = null,
            modifier = Modifier.clickable { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.height(20.dp))
        HightlightStyle("WalkWalk(멍멍) 이용약관 및 개인정보 처리방침\n")

        HightlightStyle("제1조 (목적)\n")
        nomalStyle("본 약관은 WalkWalk(멍멍)가 제공하는 모바일 애플리케이션의 이용 조건 및 절차, 이용자와 회사의 권리·의무 및 책임사항을 규정함을 목적으로 합니다.\n")

        HightlightStyle("제2조 (개인정보의 수집 및 이용에 대한 동의)\n")
        HightlightStyle("가. 수집 및 이용 목적\n")
        nomalStyle("WalkWalk(멍멍) 앱 서비스 제공 및 이용자 관리(회원가입, 로그인, 강아지 정보 관리, 활동 기록, 위치 기반 서비스 제공 등)에 필요한 최소한의 범위 내에서 개인정보를 수집합니다.\n\n 측정 데이터는 센서·기기 성능 및 네트워크 환경에 따라 오차 또는 누락이 발생할 수 있으며, 서비스는 이를 완전한 정확성으로 보증하지 않습니다.\n\n 수집된 정보는 서비스 제공 외의 용도로 사용되지 않습니다.\n")

        HightlightStyle("나. 수집 및 이용 항목\n")
        nomalStyle("필수 항목\n 이메일, 비밀번호, 강아지 이름, 몸무게, 생년월일, 나이, 프로필 사진 선택 정보, 위치정보, 활동 정보(산책 기록, 걸음 수 등)\n")
        nomalStyle("선택 항목\n 앱 내에서 제공하는 기본 프로필 사진 외 이름, 프로필 사진 등\n")

        HightlightStyle("다. 개인정보의 보유 및 이용 기간\n")
        nomalStyle("이용자가 회원으로서 서비스를 이용하는 동안 보유하며, 회원 탈퇴 시 모든 개인정보는 최대 30일까지 보관 후 파기됩니다.\n")

        HightlightStyle("라. 동의를 거부할 권리 및 불이익\n")
        nomalStyle("필수 정보 수집 및 이용에 대한 동의는 WalkWalk(멍멍) 서비스 이용을 위해 필요합니다.\n")
        nomalStyle("이용자가 필수 정보 제공에 동의하지 않을 경우, 회원가입 및 서비스 이용이 제한됩니다.\n\n" +
                "선택 항목에 대한 동의는 거부할 수 있으며, 거부하더라도 필수 서비스 이용에는 영향이 없습니다.\n")

        HightlightStyle("제3조 (위치정보 처리에 관한 사항)\n")
        nomalStyle("1. 서비스는 이용자의 실시간 위치를 기반으로 산책 거리, 이동 경로 등의 기능을 제공합니다.\n")
        nomalStyle("2. 위치정보는 일시적으로 처리되며, 산책 결과 계산 후에는 이동 거리 등 요약 데이터만 서버에 저장됩니다.\n\n" +
                "3. 회사는 이용자의 동의 없이 위치정보를 제3자에게 제공하지 않습니다.\n")

        HightlightStyle("제4조 (이용자의 의무)\n")
        nomalStyle("1. 이용자는 본 서비스를 관련 법령 및 본 약관에 따라 성실히 이용해야 하며, 다음 행위를 해서는 안 됩니다.\n")
        nomalStyle("-타인의 개인정보 도용\n" + "-서비스의 정상적인 운영을 방해하는 행위\n" + "-회사 및 제3자의 지식재산권 등 권리를 침해하는 행위\n" + "-기타 법령에 위반되는 행위\n")

        HightlightStyle("제5조 (서비스의 제공 및 변경)\n")
        nomalStyle("1. 이용자가 본 약관을 위반하거나 부정한 방법으로 서비스를 이용하는 경우, 회사는 사전 통보 없이 서비스 이용을 제한하거나 계약을 해지할 수 있습니다.\n")

        HightlightStyle("제6조 (계약 해지 및 이용 제한)\n")
        nomalStyle("1. 회사는 서비스 제공을 위해 필요한 시스템을 유지·관리하며, 부득이한 사유로 서비스의 일부 또는 전부를 변경하거나 중단할 수 있습니다.\n" +
                    "\n2. 이용자는 앱 내 “회원 탈퇴” 기능을 통해 언제든지 회원 탈퇴를 요청할 수 있으며, 회사는 관련 법령에 따라 개인정보를 즉시 파기합니다.")

        HightlightStyle("제7조 (면책조항)\n")
        nomalStyle("1. WalkWalk(멍멍)는 천재지변, 정전, 서버 또는 네트워크 장애, 제3자의 불법행위, 기타 WalkWalk(멍멍)의 합리적인 통제를 벗어난 사유로 인하여 서비스 제공에 차질이 발생한 경우 책임을 지지 않습니다.\n" +
                "\n2. 이용자의 귀책 사유로 인하여 발생한 문제에 대해서 WalkWalk(멍멍)는 책임을 지지 않습니다.\n" +
                "\n3. WalkWalk(멍멍)는 이용자 간 또는 이용자와 제3자 간에 발생한 분쟁에 개입할 의무가 없으며, 그로 인한 손해에 대해서 책임을 지지 않습니다.\n" +
                "\n4. WalkWalk(멍멍)는 서비스 내에서 제공되는 위치 정보, 이동 거리, 칼로리 소모량 등의 데이터 정확성을 완전하게 보증하지 않으며, 측정 결과의 오차나 누락에 대한 책임을 지지 않습니다.\n" +
                "WalkWalk(멍멍)는 서비스 점검, 업데이트, 개선 등을 위하여 일시적으로 서비스를 중단할 수 있습니다.\n")

        HightlightStyle("제8조 (문의처)\n")
        nomalStyle("서비스 이용, 계정 삭제, 개인정보 처리 관련 문의는 아래 이메일을 통해 접수할 수 있습니다.\n" +
                    "이메일: walkwalk0225@gmail.com")


    }

}
@Composable
fun HightlightStyle(content: String){
    Text(
        text = content,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(top = 20.dp)
    )
}
@Composable
fun nomalStyle(content: String){
    Text(
        text = content,
        style = MaterialTheme.typography.labelMedium,
        lineHeight = 20.sp,
        color = grey
    )
}
@Composable
fun smallStyle(content: String){
    Text(
        text = content,
        style = MaterialTheme.typography.labelSmall,
        lineHeight = 20.sp,
        color = middle_grey
    )
}
